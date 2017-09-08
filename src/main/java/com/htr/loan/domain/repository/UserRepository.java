package com.htr.loan.domain.repository;

import com.htr.loan.domain.Role;
import com.htr.loan.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, String>, PagingAndSortingRepository<User, String>
        , JpaSpecificationExecutor<User> {

    User findByUserAccountAndActiveTrue(String userAccount);

    List<User> findAllByActiveTrue();

    List<User> findAllByActiveTrueAndRoles_RoleName(String roleName);
}
