package com.example.demo;

public class Person {

    private final Integer born;

    private final String name;

    public Person(Integer born, String name) {
        this.born = born;
        this.name = name;
    }
    
    public String toString() {
        return name + (born != null ? " ( " + born + " ) " : "");
    }
}
