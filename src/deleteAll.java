import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

public class deleteAll {
	
    Driver driver;

    public deleteAll(String uri, String user, String password)
    {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
    
    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }  
    
    public void delete()
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
                tx.run("MATCH (n)"
                		+ "DETACH DELETE n");
                tx.success();  // Mark this write as successful.
            }
        }
    }    
    
    public void deleteSpecific(String name)
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
                tx.run("MATCH (n {name: {name}})"
                		+ "DETACH DELETE n", 
                		parameters("name", name));
                tx.success();  // Mark this write as successful.
            }
        }
    }        
    
    public static void main(String... args)
    {
    	deleteAll example = new deleteAll("bolt://localhost:7687", "neo4j", "1");
    	example.delete();
    	/*example.deleteSpecific("xuyang");*/
        example.close();
    }    
}
