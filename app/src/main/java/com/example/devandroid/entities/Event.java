package com.example.devandroid.entities;

public class Event {
    private int id;
    private TypeEvent type;
    private Dog dog;
    private String date;

    public Event(){}

    public Event(TypeEvent type, Dog dog, String date) {
        this.type = type;
        this.dog = dog;
        this.date = date;
    }

    public Event(int id, TypeEvent type, Dog dog, String date) {
        this.id = id;
        this.type = type;
        this.dog = dog;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TypeEvent getType() {
        return type;
    }

    public void setType(TypeEvent type) {
        this.type = type;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", type=" + type +
                ", dog=" + dog +
                ", date=" + date +
                '}';
    }
}
