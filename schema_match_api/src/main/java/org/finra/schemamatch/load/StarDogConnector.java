package org.finra.schemamatch.load;

import com.clarkparsia.license.Person;
import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.StardogException;
import com.complexible.stardog.api.*;
import com.complexible.stardog.api.admin.AdminConnection;
import com.complexible.stardog.api.admin.AdminConnectionConfiguration;
import org.openrdf.query.*;
import org.openrdf.query.resultio.QueryResultIO;
import org.openrdf.query.resultio.UnsupportedQueryResultFormatException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class StarDogConnector {

    protected ConnectionPool connectionPool;

    @Value("stardog.url")
    protected String url;
    @Value("stardog.username")
    protected String username;
    @Value("stardog.password")
    protected String password;
    @Value("stardog.min")
    protected int min;
    @Value("stardog.max")
    protected int max;
    @Value("stardog.expireSeconds")
    protected long expireSeconds;
    @Value("stardog.blockSeconds")
    protected long blockSeconds;

    public static final String DB_NAME = "schemamatch";


   public StarDogConnector(){
       if(url != null) {
           createDatabase(DB_NAME, url, username, password);

           ConnectionConfiguration connectionConfig = ConnectionConfiguration
                   .to(DB_NAME)
                   .server(url)
                   .reasoning(true) //TODO reasoning?
                   .credentials(username, password);
           // creates the Stardog connection pool
           connectionPool = createConnectionPool(connectionConfig, min, max, expireSeconds, blockSeconds);
       }
   }

    /**
     * Now we want to create the configuration for our pool.
     * @param connectionConfig the configuration for the connection pool
     * @return the newly created pool which we will use to get our Connections
     */
    private static ConnectionPool createConnectionPool
    (ConnectionConfiguration connectionConfig, int min, int max, long expireSeconds, long blockSeconds) {
        ConnectionPoolConfig poolConfig = ConnectionPoolConfig
                .using(connectionConfig)
                .minPool(min)
                .maxPool(max)
                .expiration(expireSeconds, TimeUnit.SECONDS)
                .blockAtCapacity(blockSeconds, TimeUnit.SECONDS);

        return poolConfig.create();
    }

    /**
     *  Creates a connection to the DBMS itself so we
     *  can perform some administrative actions.
     */
    public static void createDatabase(String dbName, String url, String username, String password) {
        try (final AdminConnection aConn = AdminConnectionConfiguration.toServer(url)
                .credentials(username, password)
                .connect()) {

            // A look at what databases are currently in Stardog
            aConn.list().forEach(item -> System.out.println(item));

            // Checks to see if the 'myNewDB' is in Stardog. If it is, we are
            // going to drop it so we are starting fresh
            if (!aConn.list().contains(dbName)) {aConn.disk(dbName).create();}
            // Convenience function for creating a persistent
            // database with all the default settings.

        }
    }

    public TupleQueryResult doQuery() throws IOException {
        Connection connection = this.connectionPool.obtain();
        TupleQueryResult tupleQueryResult = null;
        try {
            // Query the database to get our list of Marvel superheroes
            // and print the results to the console
            //TODO update query to reflect our dataset
            SelectQuery query = connection.select("PREFIX foaf:<http://xmlns.com/foaf/0.1/> select * { ?s rdf:type foaf:Person }");
            tupleQueryResult = query.execute();
            QueryResultIO.writeTuple(tupleQueryResult,
                    TextTableQueryResultWriter.FORMAT, System.out);

            while (tupleQueryResult.hasNext()) {
                // Each result is represented by a BindingSet, which corresponds to a result row
                BindingSet bindingSet = tupleQueryResult.next();

                // Each BindingSet contains one or more Bindings
                for (Binding binding : bindingSet) {
                    // Each Binding contains the variable name and the value for this result row
                    String name = binding.getName();
                    org.openrdf.model.Value value = binding.getValue();

                    System.out.println(name + " = " + value);
                }

                // Bindings can also be accessed explicitly by variable name
                //Binding binding = bindingSet.getBinding("x");
            }

            // Once we are done with a particular result we need to close it
            tupleQueryResult.close();
        } catch (StardogException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TupleQueryResultHandlerException e) {
            e.printStackTrace();
        } catch (UnsupportedQueryResultFormatException e) {
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

        return tupleQueryResult;
    }


}
