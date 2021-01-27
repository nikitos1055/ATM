package com.testATM.ATM.controller;

import com.testATM.ATM.dto.UserDTO;
import com.testATM.ATM.model.Transaction;
import com.testATM.ATM.model.User;
import com.testATM.ATM.service.TransactionService;
import com.testATM.ATM.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class UserController {
    @Autowired
    UserService serviceUser;

    @Autowired
    TransactionService serviceTransaction;

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserDTO userDTO, HttpServletRequest req) {
        if (!Objects.isNull(serviceUser.readByCardNumber(userDTO.getCardNumber()))) {
            User user = serviceUser.readByCardNumber(userDTO.getCardNumber());
            log.info("User {} was entered", user);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PostMapping("/register-user")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO, HttpServletRequest req) {
        if (Objects.isNull(serviceUser.readByCardNumber(userDTO.getCardNumber()))) {
            User user = new User(userDTO.getCardNumber(), userDTO.getPassword(), 0);
            serviceUser.create(user);
            log.info("User {} was created", user);

            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/amount")
    public ResponseEntity<?> getAmount(HttpServletRequest req) {
        User current = getData(req);
        if (Objects.nonNull(current)) {
            return new ResponseEntity<>(current.getAmount(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getAllTransactions(HttpServletRequest req) {
        User current = getData(req);
        if (Objects.nonNull(current)) {
            List<Transaction> transactions = current.getTransactions();
            if (transactions.isEmpty()) {
                log.error("No records found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/withdraw/{money}")
    public ResponseEntity<?> withdrawMoney(HttpServletRequest req, @PathVariable double money) {
        User current = getData(req);
        if (Objects.nonNull(current)) {
            if (money > current.getAmount()) {
                log.error("Not enough money - {}. ", money - current.getAmount());
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                current.setAmount(current.getAmount() - money);
                serviceUser.update(current);
                log.info("Money were withdraw : -{}.", money);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("No user authorized", HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/refill")
    public ResponseEntity<?> refillMoney(HttpServletRequest req, @RequestParam String number, @RequestParam double money) {
        User current = getData(req);
        if (Objects.nonNull(current)) {
            if (!Objects.isNull(serviceUser.readByCardNumber(number))) {
                if (!current.getCardNumber().equals(number)) {
                    User toRefill = serviceUser.readByCardNumber(number);
                    toRefill.setAmount(toRefill.getAmount() + money);
                    serviceUser.update(toRefill);
                } else {
                    current.setAmount(current.getAmount() + money);
                }
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                Transaction transaction = new Transaction(dtf.format(LocalDateTime.now()), number, money);
                current.addTransaction(serviceTransaction.create(transaction));
                serviceUser.update(current);
                log.info("Card with number : {}, was refilled for {}", number, money);
                return new ResponseEntity<>(HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("No user authorized", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/getData")
    public User getData(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }
}
