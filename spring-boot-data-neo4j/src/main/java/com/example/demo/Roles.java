package com.example.demo;

import java.util.List;

import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class Roles {

    private List<String> roles;

    @TargetNode
    private Person person;
    

    public String toString() {
        return person + " acted as " + roles;
    }
}
