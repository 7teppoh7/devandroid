package com.example.devandroid.entities;

public class Aviary {
    int id;
    String type;
    int capacity;

    public Aviary() {
    }

    public Aviary(int id, String type, int capacity) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Aviary{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
