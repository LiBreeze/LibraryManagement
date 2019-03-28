package com.example.library;

public class UserDetails {
    public UserDetails(){}
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSapid() {
        return sapid;
    }

    public void setSapid(String sapid) {
        this.sapid = sapid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public long getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(long phoneno) {
        this.phoneno = phoneno;
    }

    public UserDetails(String password, String email, String sapid, String name, String course, String stream, long phoneno,String role, String uid) {
        this.password = password;
        this.email = email;
        this.sapid = sapid;
        this.name = name;
        this.course = course;
        this.stream = stream;
        this.phoneno = phoneno;
        this.role=role;
        this.uid=uid;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String email;
    String sapid;
    String name;
    String course;
    String stream;
    String uid;
    String password;
    String role;
    long phoneno;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
