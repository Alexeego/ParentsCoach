package com.example.alexey.parentscoach.classes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;

/**
 * Created by Alexey on 15.10.2016.
 */
@JsonAutoDetect
public class Child {
    private String name;
    private boolean sex;
    private int age;
    private ArrayList<Task> tasks;

    public Child(){}

    public Child(String name, boolean sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
