package com.theironyard.users_core.model;


import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * Created by alhanger on 12/8/15.
 */
@Entity
@Table (name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column
    private List<Map<String, String>> addresses;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String phoneNum;
    @Column
    private List<Map<String, String>> phoneNumbers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
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

    public List<Map<String, String>> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Map<String, String>> addresses) {
        this.addresses = addresses;
    }

    public List<Map<String, String>> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<Map<String, String>> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}