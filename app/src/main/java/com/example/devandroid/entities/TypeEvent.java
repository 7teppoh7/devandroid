package com.example.devandroid.entities;

public class TypeEvent{
    String name;

    public TypeEvent(String name) {
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
        return "TypeEvent{" +
                "name='" + name + '\'' +
                '}';
    }
}
