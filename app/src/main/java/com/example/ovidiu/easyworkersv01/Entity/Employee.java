package com.example.ovidiu.easyworkersv01.Entity;

import java.util.Date;

/**
 * Created by ovidiu on 30/11/2017.
 */

public class Employee {
    private int id;
    private String first_name;
    private String surname;
    private Date birthday;
    private String address;
    private String phone_no;
    private String email;
    private String password;
    private int gender;
    private int status;
    private byte[] image;

    public Employee(int id, String first_name, String surname, Date birthday, int gender, String address, String phone_no, String email, String pass, int status) {
        this.id = id;
        this.first_name = first_name;
        this.surname = surname;
        this.birthday = birthday;
        this.address = address;
        this.phone_no = phone_no;
        this.email = email;
        this.password = pass;
        this.status = status;
    }

    public Employee() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
