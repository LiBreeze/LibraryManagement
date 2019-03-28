package com.example.library;

import java.io.Serializable;

class Books implements Serializable
{
    String bookname;
    String author;
    int available;

    int quantity;
    int rackno;
    String subject;
    public Books(){}


    public Books(String bookname, String author, int available, int quantity, int rackno, String subject) {
        this.bookname = bookname;
        this.author = author;
        this.available = available;
        this.quantity = quantity;
        this.rackno = rackno;
        this.subject = subject;
    }

    public int getRackno() {
        return rackno;
    }

    public void setRackno(int rackno) {
        this.rackno = rackno;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}