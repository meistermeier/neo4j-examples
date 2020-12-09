package com.example.demo;

import java.util.List;

public class Movie {

    private final String title;

    private final String tagline;

    private final List<Person> actors;

	public Movie(String title, String tagline, List<Person> actors) {
		this.title = title;
		this.tagline = tagline;
		this.actors = actors;
	}
	
	public String toString() {
		return title + ": " + tagline + " with " + actors;
	}
}
