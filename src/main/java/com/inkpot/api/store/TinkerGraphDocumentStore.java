package com.inkpot.api.store;

import com.inkpot.core.application.port.store.DocumentDto;
import com.inkpot.core.application.port.store.DocumentStore;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApplicationScoped
public class TinkerGraphDocumentStore implements DocumentStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinkerGraphDocumentStore.class);

    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String CONTENT = "content";
    public static final String DOCUMENT = "document";

    private final Graph graph;

    @Inject
    public TinkerGraphDocumentStore(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void save(DocumentDto document) {
        graph.traversal().addV(DOCUMENT)
                .property(T.id, document.getId().toString())
                .property(TITLE, document.getTitle())
                .property(AUTHOR, document.getAuthorId())
                .property(CONTENT, document.getContent())
                .iterate();
        LOGGER.info("Saved Document with id: {}", document.getId());
    }

    @Override
    public Optional<DocumentDto> find(UUID uuid) {
        return graph.traversal().V()
                .hasLabel(DOCUMENT)
                .hasId(uuid.toString())
                .tryNext()
                .map(toDocumentDto());
    }

    @Override
    public Set<DocumentDto> findAll() {
        return graph.traversal().V()
                .hasLabel(DOCUMENT)
                .toStream()
                .map(toDocumentDto())
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(UUID uuid) {
        graph.traversal().V()
                .hasLabel(DOCUMENT)
                .hasId(uuid.toString())
                .drop().iterate();
        LOGGER.info("Deleted Document with id: {}", uuid);
    }

    private Function<Vertex, DocumentDto> toDocumentDto() {
        return v -> new DocumentDto(
                UUID.fromString(v.id().toString()),
                UUID.fromString(v.property(AUTHOR).value().toString()),
                v.property(TITLE).value().toString(),
                v.property(CONTENT).value().toString());
    }

}

