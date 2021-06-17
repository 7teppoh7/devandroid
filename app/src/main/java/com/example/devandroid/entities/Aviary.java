package com.example.devandroid.entities;

public class Aviary {
    int id;
    TypeAviary type;
    String name;
    int capacity;

    public Aviary() {
    }

    public Aviary(int id, TypeAviary type, String name, int capacity) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeAviary getType() {
        return type;
    }

    public void setType(TypeAviary type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", type=" + type +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
