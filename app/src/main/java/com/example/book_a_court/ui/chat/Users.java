package com.example.book_a_court.ui.chat;

public class Users {
    String uid;
    String name;
    String email;
    String phone;
    String uri=null;

    public Users() {
    }

//    public Users(String uid, String name, String email, String phone) {
//        this.uid = uid;
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Users(String uid, String name, String email, String phone, String uri) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uri = uri;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
