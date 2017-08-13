package com.htr.loan.service.impl;

import com.htr.loan.domain.Resource;
import com.htr.loan.domain.repository.ResourceRepository;
import com.htr.loan.service.ResourceService;
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

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAllByActiveTrueAndParentResIsNull();
    }

    @Override
    public Resource saveResource(Resource resource) {
        try {
            resource.setActive(true);
            resource = resourceRepository.save(resource);
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
