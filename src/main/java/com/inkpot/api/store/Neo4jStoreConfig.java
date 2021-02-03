package com.inkpot.api.store;

import org.neo4j.configuration.GraphDatabaseSettings;
import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import java.io.File;
import java.time.Duration;

@Dependent
public class Neo4jStoreConfig {

    @Produces
    public Driver driver() {
        DatabaseManagementService managementService = new DatabaseManagementServiceBuilder( new File("neo"))
                .setConfig( BoltConnector.enabled, true )
                .setConfig( BoltConnector.listen_address, new SocketAddress( "localhost", 7687 ) )
                .setConfig( GraphDatabaseSettings.pagecache_memory, "512M" )
                .setConfig( GraphDatabaseSettings.transaction_timeout, Duration.ofSeconds( 60 ) )
                .setConfig( GraphDatabaseSettings.preallocate_logical_logs, true ).build();

        return GraphDatabase.driver("bolt://localhost:7687");
    }

}
