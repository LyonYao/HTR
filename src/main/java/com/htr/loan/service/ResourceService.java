package com.htr.loan.service;

import com.htr.loan.domain.Resource;
import com.htr.loan.domain.Role;

import java.util.List;

public interface ResourceService {

    List<Resource> findAll();

    Resource saveResource(Resource resource);
}
