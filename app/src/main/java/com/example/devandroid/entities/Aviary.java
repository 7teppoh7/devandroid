package com.example.devandroid.entities;

import java.util.ArrayList;
import java.util.List;

public class Aviary {
    private int id;
    private TypeAviary type;
    private String name;
    private int capacity;
    private List<Dog> dogs = new ArrayList<>();

    public Aviary() {
    }

    public Aviary(TypeAviary type, String name, int capacity) {
        this.type = type;
        this.name = name;
        this.capacity = capacity;
    }

    public Aviary(int id, TypeAviary type, String name, int capacity) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.capacity = capacity;
    }

    public void addDog(Dog dog){
        dogs.add(dog);
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
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
                ", dogs=" + dogs +
                '}';
    }
}
