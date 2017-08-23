package com.htr.loan.domain.repository;

import com.htr.loan.domain.BankCard;
import com.htr.loan.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BankCardRepository extends JpaRepository<BankCard, String>, JpaSpecificationExecutor<BankCard> {

    List<BankCard> findAllByActiveTrue();

    BankCard save(BankCard bankCard);

    List<BankCard> findAll();
}