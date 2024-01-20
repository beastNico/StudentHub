package com.example.studenthub;

public class Student {
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
}

