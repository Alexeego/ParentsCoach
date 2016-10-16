package com.example.alexey.parentscoach.classes;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by Alexey on 16.10.2016.
 */
@JsonAutoDetect
public class Task {
    private String title;
    private String description;
    private byte state;
    private byte child;

    public Task() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public byte getState() {
        return state;
    }

    public byte getChild() {
        return child;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public void setChild(byte child) {
        this.child = child;
    }
}
