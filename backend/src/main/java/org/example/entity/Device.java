package org.example.entity;

import lombok.Data;

@Data
public class Device {
    private int id;
    private String name;
    private String description;
    private int userId;
    private int kind;
    private String activate;
    private int active;
    // Default constructor
    public Device() {
    }

    // Parameterized constructor
    public Device(String name, String description, int userId, int kind, String activate) {
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.kind = kind;
        this.activate = activate;
        this.active = active;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getActivate() {
        return activate;
    }

    public void setActivate(String activate) {
        this.activate = activate;
    }
}