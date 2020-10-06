package com.uc.riskon.model;

public class Student {

    private String id,email,pass,fname,nim,age,address,gender;

    public Student(String id, String email, String pass, String fname, String nim, String age, String address, String gender) {
        this.id = id;
        this.email = email;
        this.pass = pass;
        this.fname = fname;
        this.nim = nim;
        this.age = age;
        this.address = address;
        this.gender = gender;
    }
    public Student(){}

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getFname() {
        return fname;
    }

    public String getNim() {
        return nim;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getGender() {
        return gender;
    }
}
