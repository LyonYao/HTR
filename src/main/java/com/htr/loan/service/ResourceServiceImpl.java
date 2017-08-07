package com.htr.loan.service;

import com.htr.loan.domain.Resource;
import com.htr.loan.domain.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

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
        }
        return null;
    }
}
