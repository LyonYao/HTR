package com.htr.loan.domain;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserAccountAndActiveTrue(String userAccount);

    User save(User user);
}
