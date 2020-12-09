package com.example.demo;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class Person {

    private final Integer born;

    @Id
    private final String name;

    public Person(Integer born, String name) {
        this.born = born;
        this.name = name;
    }
    
    public String toString() {
        return name + (born != null ? " ( " + born + " ) " : "");
    }
}
