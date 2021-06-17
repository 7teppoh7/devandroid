package com.example.devandroid.entities;

import java.sql.Date;

public class Event {
    int id;
    TypeEvent type;
    Dog dog;
    Date date;

    public Event(){}

    public Event(int id, TypeEvent type, Dog dog, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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
