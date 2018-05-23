package com.dong.noah.entity;

public class Student {
    private String name;
    private int image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Student(String name, int image) {
        this.name = name;
        this.image = image;
    }
}
