package com.example.devandroid.entities;

public class TypeAviary{
    private String name;

    public TypeAviary(String name) {
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
        return "TypeAviary{" +
                "name='" + name + '\'' +
                '}';
    }
}
