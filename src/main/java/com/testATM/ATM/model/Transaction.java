package com.testATM.ATM.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transaction_id")
    private long id;

    @Column(name = "transaction_time")
    @NotNull
    private String time;

    @Column(name = "transaction_to")
    @NotNull
    private String cardTo;

    @Column(name = "transaction_amount")
    @NotNull
    private double amount;

    public Transaction(@NotNull String time, @NotNull String cardTo, @NotNull double amount) {
        this.time = time;
        this.cardTo = cardTo;
        this.amount = amount;
    }
}
