package org.example.entity;

import lombok.Data;

@Data
public class Message {
    private int id;
    private String device;
    private int alert;
    private String info;
    private double lat;
    private double lng;
    private String stamp;
    private int value;

    // Default constructor
    public Message() {
    }

    // Parameterized constructor
    public Message(String device, int alert, String info, double lat, double lng, String stamp, int value) {
        this.device = device;
        this.alert = alert;
        this.info = info;
        this.lat = lat;
        this.lng = lng;
        this.stamp = stamp;
        this.value = value;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public int getAlert() {
        return alert;
    }

    public void setAlert(int alert) {
        this.alert = alert;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}