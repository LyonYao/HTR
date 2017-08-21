package com.htr.loan.domain.repository;

import com.htr.loan.domain.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PersonRepository extends JpaRepository<Person, String>, JpaSpecificationExecutor<Person> {

    Person save(Person person);
}
