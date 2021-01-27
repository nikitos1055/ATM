package com.testATM.ATM.service;

import com.testATM.ATM.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> allUsers();

    Transaction readById(long id);

    Transaction create(Transaction transaction);

    Transaction update(Transaction transaction);

    void delete(long id);
}
