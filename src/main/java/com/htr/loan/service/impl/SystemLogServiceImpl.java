package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.DynamicSpecifications;
import com.htr.loan.Utils.SearchFilter;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.SystemLogService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SystemLogServiceImpl implements SystemLogService {

    private static final Logger LOG = LoggerFactory.getLogger(SystemLogServiceImpl.class);

    @Autowired
    private SystemLogRepository systemLogRepository;

    @Override
    public Page<SystemLog> findAll(Map<String,Object> filterParams, Pageable pageable) {
        Map<String,SearchFilter> filterMap=SearchFilter.parse(filterParams);
        return systemLogRepository.findAll(DynamicSpecifications
                .bySearchFilter(filterMap.values(), SystemLog.class), pageable);
    }
}
