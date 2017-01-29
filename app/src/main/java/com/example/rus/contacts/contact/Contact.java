package com.example.rus.contacts.contact;

import java.io.Serializable;

/**
 * Created by RUS on 28.01.2017.
 */

public class Contact implements Serializable {

    private String uri;
    private String name;
    private String organization;
    private String position;
    private String phone;
    private String phone2;
    private String email;
    private String email2;
    private String address;
    private String address2;

    private String contact;

    public Contact() {}

    public Contact(String uri, String name, String organization, String position, String phone, String phone2, String email, String email2, String address, String address2, String contact) {
        this.uri = uri;
        this.name = name;
        this.organization = organization;
        this.position = position;
        this.phone = phone;
        this.phone2 = phone2;
        this.email = email;
        this.email2 = email2;
        this.address = address;
        this.address2 = address2;
        this.contact = contact;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return  uri + "\n" +
                name + "\n" +
                organization + "\n" +
                position + "\n" +
                phone + "\n" +
                phone2 + "\n" +
                email + "\n" +
                email2 + "\n" +
                address + "\n" +
                address2 + "\n";
    }
}
