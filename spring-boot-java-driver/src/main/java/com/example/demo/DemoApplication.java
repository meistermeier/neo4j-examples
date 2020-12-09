package com.example.demo;

import java.util.List;
import java.util.stream.Collectors;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private final Driver driver;

	@Autowired
	public DemoApplication(Driver driver) {
		this.driver = driver;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try (Session session = driver.session()) {
			List<Movie> movies = session.readTransaction(tx -> 
				tx.run("MATCH (m:Movie)<-[:ACTED_IN]-(p:Person) RETURN m, collect(p) as actors")
					.stream().map(record -> {
						Node movieNode = record.get("m").asNode();
						List<Person> actors = record.get("actors").asList(actorNode ->
							new Person(
								actorNode.get("born").isNull() ? null : actorNode.get("born").asInt(),
								actorNode.get("name").asString()
							)
						);

						return new Movie(
							movieNode.get("title").asString(),
							movieNode.get("tagline").asString(),
							actors);
					})
					.collect(Collectors.toList())
			);

			movies.forEach(System.out::println);
		}
	}

}
