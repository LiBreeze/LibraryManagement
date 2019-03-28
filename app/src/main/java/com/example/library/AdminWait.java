package com.example.library;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;

public class AdminWait {
    String date;
    long waitno;
    String email;
    String bookname;

public AdminWait(){}
    public AdminWait(String date, long waitno,String email,String bookname) {
        this.date = date;
        this.waitno = waitno;
        this.email=email;
        this.bookname=bookname;

    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getWaitno() {
        return waitno;
    }

    public void setWaitno(long waitno) {
        this.waitno = waitno;
    }

}




