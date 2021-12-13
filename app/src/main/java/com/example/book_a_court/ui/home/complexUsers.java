package com.example.book_a_court.ui.home;

public class complexUsers {
    String uid;
    String name;
    String email;
    String phone;
    String uri=null;
    float rating;

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

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public complexUsers(String uid, String name, String email, String phone, String uri, float rating) {

        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.uri = uri;
        this.rating = rating;
    }
}
