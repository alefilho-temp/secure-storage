package com.safe.storage.models;

public class SafePassword {
    int id = -1;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    int userId;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    String imagePath;
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    String accessUrl;
    public String getAccessUrl() {
        return accessUrl;
    }
    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    String note;
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
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
}
