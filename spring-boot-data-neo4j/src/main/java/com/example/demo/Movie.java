package com.example.demo;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class Movie {

	@Id
    private final String title;

    private final String tagline;

	@Relationship(type = "ACTED_IN", direction = Relationship.Direction.INCOMING)
    private final List<Roles> actors;

	public Movie(String title, String tagline, List<Roles> actors) {
		this.title = title;
		this.tagline = tagline;
		this.actors = actors;
	}
	
	public String toString() {
		return title + ": " + tagline + " with " + actors;
	}
}
