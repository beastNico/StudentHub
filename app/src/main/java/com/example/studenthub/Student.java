package com.example.studenthub;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Student implements Parcelable {
    private String name;
    private String matricNo;
    private int year;
    private int semester;
    private String major;
    private String email;

    public Student(String name, String matricNo, int year, int semester, String major, String email) {
        this.name = name;
        this.matricNo = matricNo;
        this.year = year;
        this.semester = semester;
        this.major = major;
        this.email = email;
    }

    protected Student(Parcel in) {
        name = in.readString();
        matricNo = in.readString();
        year = in.readInt();
        semester = in.readInt();
        major = in.readString();
        email = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String matricNo) {
        this.matricNo = matricNo;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(matricNo);
        dest.writeInt(year);
        dest.writeInt(semester);
        dest.writeString(major);
        dest.writeString(email);
    }
}

