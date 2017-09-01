package com.htr.loan.web;

import com.htr.loan.domain.Resource;
import com.htr.loan.domain.Role;
import com.htr.loan.domain.User;
import com.htr.loan.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @RequestMapping(value = "/currentRes", method = RequestMethod.GET)
    public List<Resource> getCurrentRes(HttpSession session) {

        User user = (User) session.getAttribute("loginUser");
        List<Resource> resources = resourceService.findAll();
        for (Role role : user.getRoles()) {
            for (Resource selectedResource : role.getResources()) {
                checkResourceSelected(resources, selectedResource);
            }
        }

        return resources;
    }

    private void checkResourceSelected(List<Resource> resources, Resource selectedResource) {
        for (Resource resource : resources) {
            if (selectedResource.getUuid().equals(resource.getUuid())) {
                resource.setSelected(true);
                break;
            } else {
                if (resource.getChildrenRes() != null) {
                    checkResourceSelected(resource.getChildrenRes(), selectedResource);
                }
            }
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Resource> findAllResources() {
        return resourceService.findAll();
    }
}
