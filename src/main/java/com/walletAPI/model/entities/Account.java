package com.walletAPI.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.walletAPI.controller.requests.AccountRequestInterface;
import com.walletAPI.json.JsonViews;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "accounts", schema = "dbo")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAccount;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false)
    @NotBlank(message = "{ \"errorCode\": \"ACCOUNT.INVALID.BLANK_NAME\", \"message\": \"Name cant be blank\"}", groups = AccountRequestInterface.class)
    @JsonView(JsonViews.Account.Patch.class)
    private String name;


    public Account(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public Account() {

    }

    @JsonIgnore
    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "\"name\":\"" + name + '\"' +
                "}";

    }


}
