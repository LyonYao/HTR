package com.htr.loan.service.impl;

import com.htr.loan.Utils.Constants;
import com.htr.loan.domain.Resource;
import com.htr.loan.domain.Role;
import com.htr.loan.domain.SystemLog;
import com.htr.loan.domain.repository.ResourceRepository;
import com.htr.loan.domain.repository.SystemLogRepository;
import com.htr.loan.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOG = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private SystemLogRepository systemLogRepository;


    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAllByActiveTrueAndParentResIsNull();
    }

    @Override
    public Resource saveResource(Resource resource) {
        try {
            SystemLog log = new SystemLog(Constants.MODULE_RESOURCE, resource.getResourceName());
            if (StringUtils.isNotBlank(resource.getUuid())) {
                log.setOperaType(Constants.OPERATYPE_UPDATE);
            } else {
                log.setOperaType(Constants.OPERATYPE_ADD);
            }
            resource = resourceRepository.save(resource);
            log.setRecordId(resource.getUuid());
            systemLogRepository.save(log);
            return resource;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update resource " + resource.getResourceName() + " fail!");
        }
        return null;
    }

    @Override
    public Resource findByResourceName(String resourceName) {
        return resourceRepository.findByResourceNameAndActiveTrue(resourceName);
    }
}
