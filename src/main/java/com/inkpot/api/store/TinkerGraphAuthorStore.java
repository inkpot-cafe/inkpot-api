package com.inkpot.api.store;

import com.inkpot.core.application.port.store.AuthorDto;
import com.inkpot.core.application.port.store.AuthorStore;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class TinkerGraphAuthorStore implements AuthorStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinkerGraphAuthorStore.class);
    public static final String AUTHOR = "author";
    public static final String NAME = "name";
    public static final String WRITES = "writes";

    private final GraphTraversalSource g;

    @Inject
    public TinkerGraphAuthorStore(GraphTraversalSource g) {
        this.g = g;
    }

    @Override
    public void save(AuthorDto author) {
        g.addV(AUTHOR)
                .property(T.id, author.getId().toString())
                .property(NAME, author.getName())
                .iterate();
        LOGGER.info("Saved Author with id: {}", author.getId());

    }

    @Override
    public Optional<AuthorDto> find(UUID uuid) {
        return g.V()
                .hasLabel(AUTHOR)
                .hasId(uuid.toString())
                .tryNext()
                .map(this::toAuthorDto);
    }

    @Override
    public Set<AuthorDto> findAll() {
        return g.V()
                .hasLabel(AUTHOR)
                .toStream()
                .map(this::toAuthorDto)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void delete(UUID uuid) {
        g.V()
                .hasLabel(AUTHOR)
                .hasId(uuid.toString())
                .drop().iterate();
        LOGGER.info("Deleted Author with id: {}", uuid);

    }

    private AuthorDto toAuthorDto(Vertex v) {
        return new AuthorDto(
                UUID.fromString(v.id().toString()),
                v.property(NAME).value().toString(),
                getDocumentIds(v)
        );
    }

    private Set<UUID> getDocumentIds(Vertex v) {
        Iterator<Edge> edges = v.edges(Direction.OUT, WRITES);
        return Stream.iterate(edges, Iterator::hasNext, UnaryOperator.identity())
                .map(Iterator::next)
                .map(e -> e.inVertex().id().toString())
                .map(UUID::fromString)
                .collect(Collectors.toSet());
    }

}
