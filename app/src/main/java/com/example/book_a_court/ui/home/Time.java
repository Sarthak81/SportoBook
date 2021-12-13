package com.example.book_a_court.ui.home;

public class Time {
    int d,m,y,hr1,hr2;

    public Time(int d, int m, int y, int hr1, int hr2) {
        this.d = d;
        this.m = m;
        this.y = y;
        this.hr1 = hr1;
        this.hr2 = hr2;
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
}
