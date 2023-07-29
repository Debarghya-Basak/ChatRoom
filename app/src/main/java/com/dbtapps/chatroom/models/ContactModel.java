package com.dbtapps.chatroom.models;

public class ContactModel {
    public String name;
    public String phoneNumber;

    public ContactModel() {
    }

    public ContactModel(String name, String phoneNumber ) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
