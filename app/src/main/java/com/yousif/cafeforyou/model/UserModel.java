package com.yousif.cafeforyou.model;

public class UserModel {
    String Email,UserName,PhoneNumber,UID,Private,Balance;

    public UserModel() {
    }

    public UserModel(String email, String userName, String phoneNumber, String UID, String aPrivate, String balance) {
        Email = email;
        UserName = userName;
        PhoneNumber = phoneNumber;
        this.UID = UID;
        Private = aPrivate;
        Balance = balance;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getPrivate() {
        return Private;
    }

    public void setPrivate(String aPrivate) {
        Private = aPrivate;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
