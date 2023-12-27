package com.example.plannr.utils;

import java.time.LocalDate;

public class Student {

    private int id;
    private String name;
    private String email;
    private LocalDate dob;
    private Integer age;

    private int online = 0;

    private boolean confirmed;

    public Student(int id, String name, String email, LocalDate dob, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.age = age;
        this.confirmed = true;
    }

    public Student(int id, String name, int online) {
        this.id = id;
        this.name = name;
        this.online = online;
    }

    // Constructor for setting default null values for majority of fields, creating a Student object with
    // only a name
    public Student (String name, String email) {
        this.id = -1;
        this.name = name;
        this.email = email;
        this.dob = null;
        this.age = -1;
        this.confirmed = true;
    }
    public Student (String name, String email, int id) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dob = null;
        this.age = -1;
        this.confirmed = true;
    }

    public Student (String name, String email, boolean confirmed) {
        this.id = -1;
        this.name = name;
        this.email = email;
        this.dob = null;
        this.age = -1;
        this.confirmed = confirmed;
    }

    public Student (String name) {
        this.id = -1;
        this.name = name;
        this.email = "";
        this.dob = null;
        this.age = -1;
        this.confirmed = true;
    }

    public Student () {
        this.id = -1;
        this.name = "";
        this.email = "";
        this.dob = null;
        this.age = -1;
        this.confirmed = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @Override
    public String toString() {
        String retString = "";
        retString = "Student{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", dob='" + dob
                + ", age=" + age + '}';
        return retString;
    }

    public int getOnline(){
        return online;
    }

    public void updateStatus(){
        if(online == 0){
            online = 1;
        }else{
            online = 0;
        }
    }
}
