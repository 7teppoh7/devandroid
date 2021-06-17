package com.example.devandroid.entities;

public class StateAnimal{
    private String name;

    public StateAnimal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StateAnimal{" +
                "name='" + name + '\'' +
                '}';
    }
}
