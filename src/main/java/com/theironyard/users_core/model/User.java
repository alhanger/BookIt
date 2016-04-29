package com.theironyard.users_core.model;


import javax.persistence.*;

/**
 * Created by alhanger on 12/8/15.
 */
@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue
    public int id;
    @Column(nullable = false)
    public String username;
    @Column(nullable = false)
    public String password;
    @Column(nullable = false)
    public String firstName;
    @Column(nullable = false)
    public String lastName;
    @Column(nullable = false)
    public String city;
    @Column(nullable = false)
    public String state;
    @Column(nullable = false)
    public String email;
    @Column(nullable = false)
    public String phoneNum;

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}