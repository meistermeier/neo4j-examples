package com.meistermeier.neo4j;

import java.util.List;
import java.util.stream.Collectors;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

public class App {
    public static void main( String[] args ) {

        Driver driver = GraphDatabase.driver("neo4j://localhost:7687", AuthTokens.basic("neo4j", "secret"));

        try (Session session = driver.session()) {
            List<String> result = session.readTransaction(tx -> {
                return tx.run("MATCH (n:Movie) return n.title as movieTitle").stream()
                .map(record -> record.get("movieTitle").asString())
                .collect(Collectors.toList());
            });

            System.out.println(result);
        }

        driver.close();
    }
}
