package com.htr.loan.domain.repository;

import com.htr.loan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserAccountAndActiveTrue(String userAccount);

    User save(User user);
}
