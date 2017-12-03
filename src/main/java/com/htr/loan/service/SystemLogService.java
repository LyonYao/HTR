package com.htr.loan.service;

import com.htr.loan.domain.SystemLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface SystemLogService {

    Page<SystemLog> findAll(Map<String, Object> filterParams, Pageable pageable);
}
