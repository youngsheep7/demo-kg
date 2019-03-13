import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

public class KnowledgeGraph {
	
    Driver driver;

    public KnowledgeGraph(String uri, String user, String password)
    {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }
    
    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }  
    
    public void queryAlarmName(String Name)
    {
        // Sessions are lightweight and disposable connection wrappers.
        try (Session session = driver.session())
        {
            // Wrapping Cypher in an explicit transaction provides atomicity
            // and makes handling errors much easier.
            try (Transaction tx = session.beginTransaction())
            {
            	/*first check existence, if nonexistence, then create*/
            	StatementResult result = tx.run("MATCH (N: AlarmInfo{AlarmName: {p1}})"
                		+ "RETURN N.AlarmName AS AlarmName, N.KChain AS KChain, N.KChainSubClass AS KChainSubClass"
                		, parameters("p1", Name));
                tx.success();  // Mark this write as successful.
                
                while (result.hasNext())
                {
                    Record record = result.next();
                    // Values can be extracted from a record by index or name.
                    System.out.println(record.get("AlarmName").asString());
                    System.out.println("此告警所处攻击阶段："+record.get("KChain").asString());
                    System.out.println(record.get("KChainSubClass").asString());
                }
            }
        }
    }     
    
    public static void main(String... args)
    {
    	KnowledgeGraph example = new KnowledgeGraph("bolt://localhost:7687", "neo4j", "1");
    	example.queryAlarmName("安全设备基本规则-信息泄露");
        example.close();
    }    
}
