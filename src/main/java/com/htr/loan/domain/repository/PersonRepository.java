package com.htr.loan.domain.repository;

import com.htr.loan.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByActiveTrue();

    Person save(Person person);
}
