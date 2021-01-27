package com.testATM.ATM.service;

import com.testATM.ATM.model.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();

    User readById(long id);

    User create(User user);

    User update(User user);

    void delete(long id);

    User readByCardNumber(String cardNumber);
}
