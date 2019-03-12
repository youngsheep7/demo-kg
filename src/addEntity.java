import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

public class addEntity {
	
    Driver driver;

    public addEntity(String uri, String user, String password)
    {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
    
    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }  
    
    public void add(String name)
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
            	/*first check existence, then create*/
                tx.run("MERGE (a:Person {name: {x}})", parameters("x", name));
                tx.success();  // Mark this write as successful.
            }
        }
    }
    
    public void addEdge(String edgeName, String node1, String node2)
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
                /*tx.run("MATCH (Person1: Person {name: {x}}), (Person2: Person {name: {y}})"
                		+ "MERGE (Person1)-[:edgeName]->(Person2)"
                		, parameters("edgeName", edgeName, "x", node1, "y", node2));*/
            	tx.run("MATCH (Person1: Person {name: {x}}), (Person2: Person {name: {y}})"
                		+ "MERGE (Person1)-[:"+edgeName+"]->(Person2)"
                		, parameters( "x", node1, "y", node2));
                tx.success();  // Mark this write as successful.
            }
        }
    }    
    
    
    public static void main(String... args)
    {
    	addEntity example = new addEntity("bolt://localhost:7687", "neo4j", "1");
    	example.add("徐旸");
    	example.add("韩悦");
    	example.addEdge("love", "徐旸", "韩悦");
        example.close();
    }    
}
