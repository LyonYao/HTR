package com.htr.loan.web;

import com.htr.loan.Utils.Constants;
import com.htr.loan.domain.Person;
import com.htr.loan.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Person> findAll(){
        return personService.findAll();
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Person savePerson(@RequestBody Person person){
        return personService.savePerson(person);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ResponseBody
    public Map<String, String> removePerson(@RequestBody List<Person> persons){
        Map<String, String> result = new HashMap<>();
        boolean isDeleted = personService.removePersons(persons);
        if(!isDeleted){
            result.put(Constants.RESPONSE_CODE, Constants.CODE_FAIL);
            result.put(Constants.RESPONSE_MSG, Constants.MSG_DELETE_FAIL);
        } else {
            result.put(Constants.RESPONSE_CODE, Constants.CODE_SUCCESS);
            result.put(Constants.RESPONSE_MSG, Constants.MSG_DELETE_SUCCESS);
        }
        return result;
    }
}
