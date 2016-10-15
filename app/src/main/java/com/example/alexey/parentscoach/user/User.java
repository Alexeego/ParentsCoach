package com.example.alexey.parentscoach.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by Alexey on 15.10.2016.
 */
@JsonAutoDetect
public class User {
    private String nickname;
    private String email;
    private String password;
    private String token;

    public User(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
