package com.testATM.ATM.service.impl;

import com.testATM.ATM.model.User;
import com.testATM.ATM.repository.UserRepository;
import com.testATM.ATM.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository rep;

    @Override
    public List<User> allUsers() {
        return rep.findAll();
    }

    @Override
    public User readById(long id) {
        Optional<User> optional = rep.findById(id);
        return optional.orElse(null);
    }

    @Override
    public User create(User user) {
        if (Objects.isNull(readById(user.getId()))) {
            return rep.save(user);
        }
        throw new EntityExistsException("User already exists.");
    }

    @Override
    public User update(User user) {
        if (!Objects.isNull(user)) {
            return rep.save(user);
        }
        throw new EntityNotFoundException("User cannot be 'null'");
    }

    @Override
    public void delete(long id) {
        if (!Objects.isNull(readById(id))) {
            rep.deleteById(id);
        }
        throw new EntityNotFoundException("User with id = " + id + ", not found. ");
    }

    @Override
    public User readByCardNumber(String cardNumber) {
        return rep.readByCardNumber(cardNumber);
    }
}
