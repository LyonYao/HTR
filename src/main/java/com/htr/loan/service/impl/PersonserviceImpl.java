package com.htr.loan.service.impl;

import com.htr.loan.domain.Person;
import com.htr.loan.domain.repository.PersonRepository;
import com.htr.loan.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PersonserviceImpl implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonserviceImpl.class);

    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person savePerson(Person person) {
        try {
            person.setActive(true);
            person = personRepository.save(person);
            return person;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("save or update Person" + person.getName() + " fail!");
        }
        return null;
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAllByActiveTrue();
    }
}
