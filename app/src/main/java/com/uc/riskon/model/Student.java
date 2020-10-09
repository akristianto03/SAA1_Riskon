package com.uc.riskon.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

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

    protected Student(Parcel in) {
        id = in.readString();
        email = in.readString();
        pass = in.readString();
        fname = in.readString();
        nim = in.readString();
        age = in.readString();
        address = in.readString();
        gender = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(pass);
        dest.writeString(fname);
        dest.writeString(nim);
        dest.writeString(age);
        dest.writeString(address);
        dest.writeString(gender);
    }
}
