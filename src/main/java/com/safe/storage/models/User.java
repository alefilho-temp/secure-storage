package com.safe.storage.models;

public class User {
    int id = -1;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public User() { }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}
