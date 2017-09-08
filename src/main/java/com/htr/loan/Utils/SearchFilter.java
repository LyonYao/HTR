package com.htr.loan.Utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class SearchFilter {

    //LIKEIGNORE 模糊查找不区分大小写
    //LIKE 模糊查找区分大小写
    public enum Operator {
        EQ, IN, ISNULL, LIKE, GT, LT, GTE, LTE, NE, LIKEIGNORE
    }

    public String fieldName;
    public Object value;
    public Operator operator;

    public SearchFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    public static Map<String, SearchFilter> parse(Map<String, Object> filterParams) {
        Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

        for (Entry<String, Object> entry : filterParams.entrySet()) {
            String[] names = entry.getKey().split("_");
            Object value = entry.getValue();
            if (names.length < 2) {
                throw new IllegalArgumentException(entry.getKey() + " is not a valid search filter name");
            }
            if (names.length == 3) {
                if ("SHORTDATE".equals(names[2]) && !ObjectUtils.isEmpty(value)) {
                    long fieldValue = (long) value;
                    Date date = new Date(fieldValue);
                    value = DateUtils.formatDate(date, DateUtils.YYYYlMMlDD);
                }
                if ("DATE".equals(names[2]) && !ObjectUtils.isEmpty(value)) {
                    long fieldValue = (long) value;
                    if ("LTE".equals(names[0])) {
                        Date date = new Date(fieldValue);
                        value = DateUtils.formatDate(date, DateUtils.YYYYLMMLDD_BIG);
                    } else if ("GTE".equals(names[0])) {
                        Date date = new Date(fieldValue);
                        value = DateUtils.formatDate(date, DateUtils.YYYYLMMLDD_SMALL);
                    }
                }
            }
            SearchFilter filter = new SearchFilter(names[1], Operator.valueOf(names[0]), value);
            if (!ObjectUtils.isEmpty(entry.getValue())) {
                filters.put(filter.fieldName + Identities.uuid2(), filter);
            }
        }
        return filters;
    }
}
