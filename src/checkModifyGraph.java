import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

public class checkModifyGraph {
	
    Driver driver;

    public checkModifyGraph(String uri, String user, String password)
    {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
    
    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }  
    
    public void matchReturn(String keywords)
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
            	/*first check existence, then create*/
            	StatementResult result = tx.run("MATCH (N: Person{name: {x}})"
                		+ "RETURN N.name AS name", parameters("x", keywords));
                tx.success();  // Mark this write as successful.
                
                while (result.hasNext())
                {
                    Record record = result.next();
                    // Values can be extracted from a record by index or name.
                    System.out.println(record.get("name").asString());
                }
                
            }
        }
    } 
    
    public void modify(String keywords, String age)
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
            	/*first check existence, then create*/
            	StatementResult result = tx.run("MATCH (N: Person{name: {x}})"
                		+ "SET N.age = {age}", parameters("x", keywords, "age", age));
                tx.success();  // Mark this write as successful.
                
            }
        }
    }     
    
    public static void main(String... args)
    {
    	checkModifyGraph example = new checkModifyGraph("bolt://localhost:7687", "neo4j", "1");
    	example.matchReturn("徐旸");
    	example.modify("徐旸", "18");
        example.close();
    }    
}
