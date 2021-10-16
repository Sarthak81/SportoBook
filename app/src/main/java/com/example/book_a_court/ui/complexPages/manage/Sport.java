package com.example.book_a_court.ui.complexPages.manage;

public class Sport {
    String Name;
    String Price;
    String num_of_courts;

    public Sport(String name, String price, String num_of_courts) {
        Name = name;
        Price = price;
        this.num_of_courts = num_of_courts;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getNum_of_courts() {
        return num_of_courts;
    }

    public void setNum_of_courts(String num_of_courts) {
        this.num_of_courts = num_of_courts;
    }
}
