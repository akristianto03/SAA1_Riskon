package com.uc.riskon.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Course implements Parcelable {

    private String id;
    private String subject;
    private String day;
    private String timeStart;
    private String timeEnd;
    private String lecturer;

    public Course(){}

    public Course(String id, String subject, String day, String timeStart, String timeEnd, String lecturer) {
        this.id = id;
        this.subject = subject;
        this.day = day;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.lecturer = lecturer;
    }

    protected Course(Parcel in) {
        id = in.readString();
        subject = in.readString();
        day = in.readString();
        timeStart = in.readString();
        timeEnd = in.readString();
        lecturer = in.readString();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(subject);
        dest.writeString(day);
        dest.writeString(timeStart);
        dest.writeString(timeEnd);
        dest.writeString(lecturer);
    }
}
