package com.htr.loan.service;

import com.htr.loan.domain.Person;

import java.util.List;

public interface PersonService {

    Person savePerson(Person person);

    List<Person> findAll();

    boolean removePersons(List<Person> personList);
}
