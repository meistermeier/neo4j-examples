///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli-codegen:4.5.0 org.neo4j.driver:neo4j-java-driver:4.2.0 org.reactivestreams:reactive-streams:1.0.2

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;

@Command(name = "hello_neo4j", mixinStandardHelpOptions = true, version = "hello_neo4j 0.1",
        description = "hello_neo4j made with jbang")
class hello_neo4j implements Callable<Integer> {

    private static final String QUERY = ""
    + "MATCH (otherMovies:Movie)<-[otherR:ACTED_IN]-(person:Person)-[r:ACTED_IN]->(m:Movie) "
    + "WHERE $role IN r.roles AND NOT $role IN otherR.roles "
    + "RETURN collect(distinct m.title) as titles, collect(distinct otherMovies.title) as otherMovieTitles, person";

    @Parameters(index = "0", description = "The role name of the person in a certain movie")
    private String role;

    public static void main(String... args) {
        int exitCode = new CommandLine(new hello_neo4j()).execute(args);
        System.exit(exitCode);
    }


    @Override
    public Integer call() throws Exception {
        System.out.println("Searching for: " + role);

        Driver driver = GraphDatabase.driver("neo4j://localhost:7687", AuthTokens.basic("neo4j", "secret"));
        
        try (Session session = driver.session()) {
            List<RoleInfo> roleInfos = session.readTransaction(tx -> 
                tx.run(QUERY, Values.parameters("role", role))
                .stream().map(record -> 
                new RoleInfo(role, 
                    record.get("titles").asList(Function.identity()).stream().map(value -> value.asString()).collect(Collectors.toList()),
                    record.get("otherMovieTitles").asList(Function.identity()).stream().map(value -> value.asString()).collect(Collectors.toList()),
                    record.get("person").get("name").asString())
                    )
                .collect(Collectors.toList())
            );

            roleInfos.forEach(System.out::println);
        }

        driver.close();
        return 0;
    }

    static class RoleInfo {
        private final String roleName;
        private final List<String> roleInMovies;
        private final String actorName;
        private final List<String> otherMovies;

        public RoleInfo(String roleName, List<String> roleInMovies, List<String> otherMovies, String actorName) {
                this.roleName = roleName;
                this.roleInMovies = roleInMovies;
                this.actorName = actorName;
                this.otherMovies = otherMovies;
        }

        public String toString() {
                return "Role: " + roleName + "\n"
                + "Actor " + actorName + "\n"
                + "playing this role in " + roleInMovies + "\n"
                + "also played in " + otherMovies + "\n";
        }
	}
}
