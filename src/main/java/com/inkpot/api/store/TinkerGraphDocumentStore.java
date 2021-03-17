package com.inkpot.api.store;

import com.inkpot.core.application.port.store.DocumentDto;
import com.inkpot.core.application.port.store.DocumentStore;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class TinkerGraphDocumentStore implements DocumentStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinkerGraphDocumentStore.class);

    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String DOCUMENT = "document";
    public static final String WRITES = "writes";

    private final GraphTraversalSource g;

    @Inject
    public TinkerGraphDocumentStore(GraphTraversalSource g) {
        this.g = g;
    }

    @Override
    public void save(DocumentDto document) {
        g.addV(DOCUMENT)
                .as(DOCUMENT)
                .property(T.id, document.getId().toString())
                .property(TITLE, document.getTitle())
                .property(CONTENT, document.getContent())
                .V()
                .hasLabel(AUTHOR)
                .hasId(document.getAuthorId().toString())
                .as(AUTHOR)
                .addE(WRITES).from(AUTHOR).to(DOCUMENT)
                .iterate();
        LOGGER.info("Saved Document with id: {}", document.getId());
    }

    @Override
    public Optional<DocumentDto> find(UUID id) {
        return g.V()
                .hasLabel(DOCUMENT)
                .hasId(id.toString())
                .tryNext()
                .map(this::toDocumentDto);
    }

    @Override
    public Set<DocumentDto> findAll() {
        return g.V()
                .hasLabel(DOCUMENT)
                .toStream()
                .map(this::toDocumentDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(UUID id) {
        g.V()
                .hasLabel(DOCUMENT)
                .hasId(id.toString())
                .drop().iterate();
        LOGGER.info("Deleted Document with id: {}", id);
    }

    private DocumentDto toDocumentDto(Vertex v) {
        UUID authorId = UUID.fromString(v.edges(Direction.IN, WRITES).next().outVertex().id().toString());
        return new DocumentDto(
                UUID.fromString(v.id().toString()),
                authorId,
                v.property(TITLE).value().toString(),
                v.property(CONTENT).value().toString());
    }

}

