package com.example.library;

import java.io.Serializable;

public class Taken implements Serializable {
    String issue_date,expected_date,return_date,bookname,useremail,uid;

    public Taken(){}

    public Taken(String issue_date, String expected_date, String return_date,String bookname,String useremail,String uid) {
        this.issue_date = issue_date;
        this.expected_date = expected_date;
        this.return_date = return_date;
        this.bookname=bookname;
        this.useremail=useremail;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }

    public String getExpected_date() {
        return expected_date;
    }

    public void setExpected_date(String expected_date) {
        this.expected_date = expected_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
}

