package com.htr.loan.service.impl;

import com.htr.loan.Utils.DynamicSpecifications;
import com.htr.loan.Utils.SearchFilter;
import com.htr.loan.domain.Person;
import com.htr.loan.domain.repository.PersonRepository;
import com.htr.loan.service.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonServiceImpl.class);

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
    public boolean removePersons(List<Person> personList) {
        try {
            for(Person person : personList){
                person.setActive(false);
                personRepository.save(person);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("delete Person fail!");
        }
        return false;
    }

    @Override
    public Page<Person> findAll(Map<String,Object> filterParams, Pageable pageable) {
        Map<String,SearchFilter> filterMap=SearchFilter.parse(filterParams);
        return personRepository.findAll(DynamicSpecifications
                .bySearchFilter(filterMap.values(), Person.class), pageable);
    }
}
