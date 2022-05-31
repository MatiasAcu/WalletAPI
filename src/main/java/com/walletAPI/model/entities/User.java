package com.walletAPI.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.walletAPI.json.JsonViews;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users", schema = "dbo")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "{ \"errorCode\": \"USER.INVALID.BLANK_EMAIL\", \"message\": \"Email cant be blank\"}")
    @Email(message = "{ \"errorCode\": \"USER.INVALID.EMAIL\", \"message\": \"Email has to be valid\"}")
    @JsonView(JsonViews.User.Patch.class)
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "{ \"errorCode\": \"USER.INVALID.BLANK_NAME\", \"message\": \"Username cant be blank\"}")
    @JsonView(JsonViews.User.Patch.class)
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "{ \"errorCode\": \"USER.INVALID.BLANK_PASSWORD\", \"message\": \"Password cant be blank\"}")
    @JsonView(JsonViews.User.Patch.class)
    @Size(min = 8, message = "{ \"errorCode\": \"USER.INVALID.SIZE_PASSWORD\", \"message\": \"Password has to be at least 8 characters long\"}")
    private String password;

    public User() {
    }


    public User(String username, String password, String email) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    @JsonIgnore
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "{" +
                "\"username\":\"" + username + '"' +
                ", \"email\":\"" + email + '"' +
                "}";
    }

}

