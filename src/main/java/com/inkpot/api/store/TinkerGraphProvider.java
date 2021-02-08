package com.inkpot.api.store;

import io.quarkus.runtime.Startup;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Produces;
import java.util.Optional;

@Startup
@ApplicationScoped
public class TinkerGraphProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinkerGraphProvider.class);

    private final Optional<String> graphLocation;
    private TinkerGraph graph;

    @Inject
    public TinkerGraphProvider(@ConfigProperty(name = "tinkergraph.graphLocation") Optional<String> graphLocation) {
        this.graphLocation = graphLocation;
    }

    @Startup
    @Produces
    @ApplicationScoped
    public Graph instantiateGraph() {
        Configuration configuration = new BaseConfiguration();
        configuration.setProperty(Graph.GRAPH, "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph");
        if (graphLocation.isPresent()) {
            configuration.setProperty("gremlin.tinkergraph.graphLocation", graphLocation.get());
            configuration.setProperty("gremlin.tinkergraph.graphFormat", "gryo");
        }
        graph = TinkerGraph.open(configuration);
        LOGGER.info("TinkerGraph created");
        return graph;
    }

    @PreDestroy
    public void tearDown() {
        try {
            graph.close();
            LOGGER.info("TinkerGraph closed successfully");
        } catch (Exception e) {
            LOGGER.error("Error closing TinkerGraph", e);
        }
    }

}
