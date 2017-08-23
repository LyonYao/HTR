package com.htr.loan.service;


import com.htr.loan.domain.BankCard;

import java.util.List;

public interface BankCardService {

    List<BankCard> findAllBankCard();

    List<BankCard> findAllByActiveTrue();

    BankCard saveBankCard(BankCard bankCard);

    boolean stopBankCards(List<BankCard> bankCards);
}
