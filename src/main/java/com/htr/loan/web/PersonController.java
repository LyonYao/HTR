package com.htr.loan.web;

import com.htr.loan.domain.Person;
import com.htr.loan.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
