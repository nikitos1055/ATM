package com.testATM.ATM.service.impl;

import com.testATM.ATM.model.Transaction;
import com.testATM.ATM.repository.TransactionRepository;
import com.testATM.ATM.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionalServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository rep;

    @Override
    public List<Transaction> allUsers() {
        return rep.findAll();
    }

    @Override
    public Transaction readById(long id) {
        Optional<Transaction> transaction = rep.findById(id);
        if (transaction.isPresent()) {
            return transaction.get();
        }
        throw new EntityNotFoundException("Transaction with id " + id + " not found");
    }

    @Override
    public Transaction create(Transaction transaction) {
        return rep.save(transaction);
    }

    @Override
    public Transaction update(Transaction transaction) {
        if (Objects.nonNull(transaction)) {
            return rep.save(transaction);
        }
        throw new EntityNotFoundException("Transaction cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        if (Objects.nonNull(readById(id))) {
            rep.deleteById(id);
        }
        throw new EntityNotFoundException("Transaction cannot be 'null'");
    }
}
