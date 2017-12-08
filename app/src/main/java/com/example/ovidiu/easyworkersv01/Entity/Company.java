package com.example.ovidiu.easyworkersv01.Entity;

/**
 * Created by anamali on 01/12/2017.
 */

public class Company {
    private int id;
    private String regNum;
    private String name;
    private String phoneNum;
    private String password;
    private String email;
    private String address;


    public Company(){

    }

    public Company(int id) {
        this.id = id;
    }

    public Company(int id, String regNum, String name, String phoneNum, String address, String email, String password){
        this.id = id;
        this.regNum = regNum;
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
        this.email = email;
        this.password = password;

    }

    public int getId() {
        return id;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }
}
