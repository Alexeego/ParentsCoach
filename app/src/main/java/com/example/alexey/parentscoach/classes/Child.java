package com.example.alexey.parentscoach.classes;

/**
 * Created by Alexey on 15.10.2016.
 */
public class Child {
    private String name;
    private boolean sex;
    private int age;

    public Child(String name, boolean sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
