package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.DateUtils;
import com.htr.loan.Utils.DynamicSpecifications;
import com.htr.loan.Utils.LoanInfoHelper;
import com.htr.loan.Utils.MoneyCalculator;
import com.htr.loan.Utils.SearchFilter;
import com.htr.loan.domain.LoanInfo;
import com.htr.loan.domain.LoanRecord;
import com.htr.loan.domain.PhoneInfo;
import com.htr.loan.domain.SubLoanRecord;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.LoanInfoRepository;
import com.htr.loan.domain.repository.SubLoanRecordRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.LoanInfoService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class LoanInfoServiceImpl implements LoanInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(LoanInfoServiceImpl.class);

    @Autowired
    private LoanInfoRepository loanInfoRepository;

    @Autowired
    private SubLoanRecordRepository subLoanRecordRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public LoanInfo saveLoanInfo(LoanInfo loanInfo) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_LOANINFO, loanInfo.getLoanInfoNum());
            log.setOperaType(Constants.OPERATYPE_ADD);
            loanInfo = loanInfoRepository.save(loanInfo);
            LoanRecord nextRepay = LoanInfoHelper.checkTheNextRepay(loanInfo);
            loanInfo.setNextRepay(nextRepay);
            loanInfo.setLeftDays(DateUtils.between(nextRepay.getExpectDate(), LocalDate.now()));
            loanInfo = loanInfoRepository.save(loanInfo);
            log.setRecordId(loanInfo.getUuid());
            systemLogRepository.save(log);
            return loanInfo;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update LoanInfo" + loanInfo.getLoanInfoNum() + " fail!");
        }
        return null;
    }

    @Override
    public boolean removeLoanInfos(List<LoanInfo> loanInfoList) {
        try {
            SystemLog log;
            for (LoanInfo loanInfo : loanInfoList) {
                log = new SystemLog(Constants.MODULE_LOANINFO, loanInfo.getLoanInfoNum(), loanInfo.getUuid(), Constants.OPERATYPE_DELETE);
                loanInfo.setActive(false);
                loanInfo.getLoanRecords().forEach(loanRecord -> loanRecord.setActive(false));
                List<SubLoanRecord> subLoanRecords = subLoanRecordRepository.findAllByLoanInfoAndActiveTrue(loanInfo);
                subLoanRecords.forEach(subLoanRecord -> subLoanRecord.setActive(false));
                subLoanRecordRepository.save(subLoanRecords);
                loanInfoRepository.save(loanInfo);
                systemLogRepository.save(log);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("delete LoanInfo fail!");
        }
        return false;
    }

    @Override
    public Page<LoanInfo> findAll(Map<String, Object> filterParams, Pageable pageable) {
        Map<String, SearchFilter> filterMap = SearchFilter.parse(filterParams);
        return loanInfoRepository.findAll(DynamicSpecifications
                .bySearchFilter(filterMap.values(), LoanInfo.class), pageable);
    }

    @Override
    public HSSFWorkbook exportMonthReport() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("月报表");

        //写入表头
        createMonthReportTitle(workbook, sheet);

        List<LoanInfo> loanInfos = loanInfoRepository.findAllByActiveTrue();

        int rowNum = 1;
        for (LoanInfo loanInfo : loanInfos) {
            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(loanInfo.getLoanInfoNum());
            row.createCell(2).setCellValue(loanInfo.getVehicle().getHolder().getName());

            Double tempExpectMoney = 0d;
            Double tempExpectMoneyTotle = 0d;
            for (LoanRecord loanRecord : loanInfo.getLoanRecords()) {
                if (DateUtils.isThisMonth(loanRecord.getExpectDate())) {
                    tempExpectMoney = loanRecord.getExpectMoney();
                    tempExpectMoneyTotle = MoneyCalculator.add(tempExpectMoneyTotle, tempExpectMoney);
                } else if (loanRecord.getExpectDate().getTime() < new Date().getTime()) {
                    tempExpectMoneyTotle = MoneyCalculator.add(tempExpectMoneyTotle, loanRecord.getExpectMoney());
                }
            }
            row.createCell(4).setCellValue(tempExpectMoney);

            Double tempReceipts = 0d;
            Double tempReceiptsTotle = 0d;
            for (SubLoanRecord subLoanRecord : subLoanRecordRepository.findAllByLoanInfoAndActiveTrue(loanInfo)) {
                if (DateUtils.isThisMonth(subLoanRecord.getReceiptDate())) {
                    tempReceipts = MoneyCalculator.add(tempReceipts, subLoanRecord.getReceipts());
                }
                tempReceiptsTotle = MoneyCalculator.add(tempReceiptsTotle, subLoanRecord.getReceipts());
            }
            row.createCell(5).setCellValue(tempReceipts);
            row.createCell(6).setCellValue(MoneyCalculator.subtract(tempExpectMoney, tempReceipts));
            row.createCell(7).setCellValue(MoneyCalculator.subtract(tempExpectMoneyTotle, tempReceiptsTotle));
            row.createCell(8).setCellValue(tempReceiptsTotle);

            row.createCell(3).setCellValue(MoneyCalculator.subtract(tempReceiptsTotle, tempReceipts));

            rowNum++;
        }

        //合计
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        HSSFRow row = sheet.createRow(rowNum);
        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("合计");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(D2:D" + rowNum + ")");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(E2:E" + rowNum + ")");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(F2:F" + rowNum + ")");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(G2:G" + rowNum + ")");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(H2:H" + rowNum + ")");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(I2:I" + rowNum + ")");
        cell.setCellStyle(style);

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, 0, 2);
        sheet.addMergedRegion(cellRangeAddress);
        return workbook;
    }


    @Override
    public HSSFWorkbook exportLoanInfo() {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("客户欠款单");
        //写入表头
        createLoanInfoTitle(workbook, sheet);

        List<LoanInfo> loanInfos = loanInfoRepository.findAllByActiveTrueAndCompletedFalse();

        int rowNum = 1;
        for (LoanInfo loanInfo : loanInfos) {
            if(loanInfo.getLeftDays() >= 0) {
                continue;
            }

            HSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(loanInfo.getLoanInfoNum());
            row.createCell(2).setCellValue(loanInfo.getVehicle().getHolder().getName());

            String phoneNumbers = "";
            for (PhoneInfo phoneInfo : loanInfo.getVehicle().getHolder().getPhoneInfos()) {
                phoneNumbers += phoneInfo.getDescription() + phoneInfo.getPhoneNum() + "/";
            }
            if(!phoneNumbers.equals("")){
                row.createCell(3).setCellValue(phoneNumbers.substring(0, phoneNumbers.length()-1));
            }
            row.createCell(4).setCellValue(loanInfo.getVehicle().getHolder().getAddress());
            row.createCell(5).setCellValue(loanInfo.getVehicle().getBrand());
            row.createCell(6).setCellValue(loanInfo.getVehicle().getLicensePlate());

            HSSFCellStyle style=workbook.createCellStyle();
            style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
            HSSFCell cell = row.createCell(7);
            cell.setCellValue(loanInfo.getVehicle().getRegistrationDate());
            cell.setCellStyle(style);

            row.createCell(8).setCellValue(loanInfo.getLoanAmount());
            row.createCell(9).setCellValue(loanInfo.getLoansNum());
            row.createCell(10).setCellValue(loanInfo.getNextRepay().getLoanNum() - 1);

            Double tempReceiptsTotle = 0d;
            for (SubLoanRecord subLoanRecord : subLoanRecordRepository.findAllByLoanInfoAndActiveTrue(loanInfo)) {
                tempReceiptsTotle = MoneyCalculator.add(tempReceiptsTotle, subLoanRecord.getReceipts());
            }
            row.createCell(11).setCellValue(tempReceiptsTotle);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(loanInfo.getNextRepay().getExpectDate());
            row.createCell(12).setCellValue("每月 " + calendar.get(Calendar.DATE) + " 日");
            row.createCell(13).setCellValue(loanInfo.getNextRepay().getExpectMoney());
            String tempMonths = "";
            for (LoanRecord loanRecord : loanInfo.getLoanRecords()) {
                if (!loanRecord.isCompleted() && (loanRecord.getExpectDate().getTime() < new Date().getTime())) {
                    Calendar expectDate = Calendar.getInstance();
                    expectDate.setTime(loanRecord.getExpectDate());
                    tempMonths += (expectDate.get(Calendar.MONTH) + 1) + "月,";
                }
            }
            row.createCell(14).setCellValue(tempMonths.substring(0, tempMonths.length() - 1));

            Double tempExpectMoneyTotle = 0d;
            for (LoanRecord loanRecord : loanInfo.getLoanRecords()) {
                if (DateUtils.isThisMonth(loanRecord.getExpectDate())) {
                    tempExpectMoneyTotle = MoneyCalculator.add(tempExpectMoneyTotle, loanRecord.getExpectMoney());
                } else if (loanRecord.getExpectDate().getTime() < new Date().getTime()) {
                    tempExpectMoneyTotle = MoneyCalculator.add(tempExpectMoneyTotle, loanRecord.getExpectMoney());
                }
            }
            row.createCell(15).setCellValue(MoneyCalculator.subtract(tempExpectMoneyTotle, tempReceiptsTotle));
            rowNum++;
        }

        //合计
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        HSSFRow row = sheet.createRow(rowNum);
        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("合计");
        cell.setCellStyle(style);

        cell = row.createCell(15);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula("SUM(P2:P" + rowNum + ")");
        cell.setCellStyle(style);

        CellRangeAddress cellRangeAddress = new CellRangeAddress(rowNum, rowNum, 0, 14);
        sheet.addMergedRegion(cellRangeAddress);

        return workbook;
    }

    /***
     * 创建表头
     * @param workbook
     * @param sheet
     */
    private void createMonthReportTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);

        sheet.setColumnWidth(1, 17 * 256);
        sheet.setColumnWidth(2, 17 * 256);
        sheet.setColumnWidth(3, 17 * 256);
        sheet.setColumnWidth(4, 17 * 256);
        sheet.setColumnWidth(5, 17 * 256);
        sheet.setColumnWidth(6, 17 * 256);
        sheet.setColumnWidth(7, 17 * 256);
        sheet.setColumnWidth(8, 17 * 256);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("档案号");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("姓名");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("上月累计收入");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("本期应收");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("本期实收");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("差额");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("累计差额");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("本月累计收入");
        cell.setCellStyle(style);
    }

    /***
     * 创建表头
     * @param workbook
     * @param sheet
     */
    private void createLoanInfoTitle(HSSFWorkbook workbook, HSSFSheet sheet) {
        HSSFRow row = sheet.createRow(0);

        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("档案号");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("客户姓名");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("联系电话");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("地址");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("车型");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("车牌号");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("上户日期");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("贷款额");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("贷款期数");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("已还期数");
        cell.setCellStyle(style);

        cell = row.createCell(11);
        cell.setCellValue("已还总额");
        cell.setCellStyle(style);

        cell = row.createCell(12);
        cell.setCellValue("还款时间");
        cell.setCellStyle(style);

        cell = row.createCell(13);
        cell.setCellValue("每月还款金额");
        cell.setCellStyle(style);

        cell = row.createCell(14);
        cell.setCellValue("欠款月份");
        cell.setCellStyle(style);

        cell = row.createCell(15);
        cell.setCellValue("总欠款金额");
        cell.setCellStyle(style);

        cell = row.createCell(16);
        cell.setCellValue("备注");
        cell.setCellStyle(style);
    }
}
