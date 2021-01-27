package com.testATM.ATM.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;

    @Column(name = "user_card_number", length = 16, unique = true)
    @NotNull
    private String cardNumber;

    @Column(name = "user_password", length = 4)
    @NotNull
    private String password;

    @Column(name = "user_amount")
    @NotNull
    private double amount;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role = Role.USER;

    @ManyToMany
    @JoinTable(
            name = "transactions",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id"))
    private List<Transaction> transactions = new ArrayList<>();

    public User(@NotNull String cardNumber, @NotNull String password, @NotNull double amount) {
        this.cardNumber = cardNumber;
        this.password = password;
        this.amount = amount;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
