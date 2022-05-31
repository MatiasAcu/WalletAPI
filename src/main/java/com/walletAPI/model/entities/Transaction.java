package com.walletAPI.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.walletAPI.json.JsonViews;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;


@Entity
@Table(name = "transactions", schema = "dbo")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    @JsonProperty(value = "id")
    private Long idTransaction;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_account", nullable = false, updatable = false)
    private Account account;

    @Column(nullable = false)
    @NotNull(message = "{ \"errorCode\": \"TRANSACTION.INVALID.NULL_AMOUNT\", \"message\": \"Amount cant be null\"}", groups = TransactionRequiredException.class)
    @JsonView(JsonViews.Transaction.Patch.class)
    private float amount;

    @Column(nullable = false)
    @JsonView(JsonViews.Transaction.Patch.class)
    @NotBlank(message = "{ \"errorCode\": \"TRANSACTION.INVALID.BLANK_DESCRIPTION\", \"message\": \"Description cant be blank\"}", groups = TransactionRequiredException.class)
    private String description;

    @Column(nullable = false)
    @JsonView(JsonViews.Transaction.Patch.class)
    private Date date;

    public Transaction() {
    }

    public Transaction(Account account, float amount, String description, Timestamp date) {
        this.account = account;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public Long getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(Long idTransaction) {
        this.idTransaction = idTransaction;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + idTransaction +
                ", \"amount\":" + amount +
                ", \"description\":\"" + description + '\"' +
                ", \"date\":\"" + date.toInstant() + '\"' +
                "}";
    }
}