package com.inkpot.api.store;

import com.inkpot.core.store.DocumentDto;
import com.inkpot.core.store.DocumentStore;
import io.quarkus.runtime.Startup;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
@Startup
public class TinkerGraphStore implements DocumentStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinkerGraphStore.class);
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String DOCUMENT = "document";

    @ConfigProperty(name = "tinkergraph.graphLocation")
    String graphLocation;

    private Graph graph;

    @PostConstruct
    public void startUp() {
        Configuration configuration = new BaseConfiguration();
        configuration.setProperty(Graph.GRAPH, "org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph");
        configuration.setProperty("gremlin.tinkergraph.graphLocation", graphLocation);
        configuration.setProperty("gremlin.tinkergraph.graphFormat", "gryo");
        graph = TinkerGraph.open(configuration);
        LOGGER.info("TinkerGraph created");
        LOGGER.debug("Working Directory = " + System.getProperty("user.dir"));
    }

    @PreDestroy
    public void shutdown() {
        try {
            graph.close();
            LOGGER.info("TinkerGraph closed successfully");
        } catch (Exception e) {
            LOGGER.error("Error closing TinkerGraph");
        }
    }

    @Override
    public void save(DocumentDto document) {
        graph.traversal().addV(DOCUMENT)
                .property(T.id, document.getUuid().toString())
                .property(TITLE, document.getTitle())
                .property(AUTHOR, document.getAuthor())
                .property(CONTENT, document.getContent())
                .iterate();
        LOGGER.info("Saved Document with id: {}", document.getUuid());
    }

    @Override
    public DocumentDto find(UUID uuid) {
        return graph.traversal().V()
                .as(DOCUMENT).hasId(uuid.toString())
                .tryNext()
                .map(toDocumentDto())
                .orElse(null);
    }

    @Override
    public Set<DocumentDto> findAll() {
        return graph.traversal().V()
                .as(DOCUMENT)
                .toStream()
                .map(toDocumentDto())
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(UUID uuid) {
        graph.traversal().V()
                .as(DOCUMENT).hasId(uuid.toString())
                .drop().iterate();
        LOGGER.info("Deleted Document with id: {}", uuid);
    }

    private Function<Vertex, DocumentDto> toDocumentDto() {
        return v -> new DocumentDto(
                UUID.fromString(v.id().toString()),
                v.property(AUTHOR).value().toString(),
                v.property(TITLE).value().toString(),
                v.property(CONTENT).value().toString());
    }
}

