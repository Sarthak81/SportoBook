package com.example.book_a_court.ui.home;

public class BookingData {
    int d,m,y,hr1,hr2,fare;
    String date,time,sportName,name;

    public BookingData() {
    }

    public BookingData(int d, int m, int y, int hr1, int hr2, int fare, String date, String time, String sportName, String name) {
        this.d = d;
        this.m = m;
        this.y = y;
        this.hr1 = hr1;
        this.hr2 = hr2;
        this.fare = fare;
        this.date = date;
        this.time = time;
        this.sportName = sportName;
        this.name = name;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHr1() {
        return hr1;
    }

    public void setHr1(int hr1) {
        this.hr1 = hr1;
    }

    public int getHr2() {
        return hr2;
    }

    public void setHr2(int hr2) {
        this.hr2 = hr2;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
