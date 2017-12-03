package com.htr.loan.service;

import com.htr.loan.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PersonService {

    Person savePerson(Person person);

    Page<Person> findAll(Map<String,Object> filterParams, Pageable pageable);

    boolean removePersons(List<Person> personList);
}
