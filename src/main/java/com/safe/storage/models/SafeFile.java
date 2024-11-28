package com.safe.storage.models;

public class SafeFile {
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

    String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    String originalName;
    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    String note;
    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    byte[] data;
    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data) {
        this.data = data;
    }

    long size;
    public long getSize() {
        return size;
    }
    public void setSize(long size) {
        this.size = size;
    }
}
