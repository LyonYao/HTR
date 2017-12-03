package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.Utils.WebUtil;
import com.htr.loan.domain.Person;
import com.htr.loan.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/{currentPage}/{pageSize}", method = RequestMethod.GET)
    public Page<Person> findAll(@PathVariable int currentPage,
                                @PathVariable int pageSize,
                                @RequestParam(defaultValue = "{}") String jsonFilter){
        Map<String, Object> filterParams = WebUtil.getParametersStartingWith(jsonFilter, Constants.SEARCH_PREFIX);
        filterParams.put("EQ_active", Constants.RECORD_EXIST);

        return personService.findAll(filterParams, WebUtil.buildPageRequest(currentPage, pageSize));
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Person savePerson(@RequestBody Person person){
        return personService.savePerson(person);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> removePerson(@RequestBody List<Person> persons){
        boolean isDeleted = personService.removePersons(persons);
        return WebUtil.buildDeleteMethodResult(isDeleted);
    }
}
