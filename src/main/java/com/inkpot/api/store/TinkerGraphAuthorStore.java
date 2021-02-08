package com.inkpot.api.store;

import com.inkpot.core.application.port.store.AuthorDto;
import com.inkpot.core.application.port.store.AuthorStore;
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
import java.util.stream.Collectors;

@ApplicationScoped
public class TinkerGraphAuthorStore implements AuthorStore {

    private static final Logger LOGGER = LoggerFactory.getLogger(TinkerGraphAuthorStore.class);
    public static final String AUTHOR = "author";
    public static final String NAME = "name";

    private final Graph graph;

    @Inject
    public TinkerGraphAuthorStore(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void save(AuthorDto author) {
        graph.traversal().addV(AUTHOR)
                .property(T.id, author.getUuid().toString())
                .property(NAME, author.getName())
                .iterate();
        LOGGER.info("Saved Author with id: {}", author.getUuid());

    }

    @Override
    public Optional<AuthorDto> find(UUID uuid) {
        return graph.traversal().V()
                .as(AUTHOR).hasId(uuid.toString())
                .tryNext()
                .map(this::toAuthorDto);
    }

    @Override
    public Set<AuthorDto> findAll() {
        return graph.traversal().V()
                .as(AUTHOR)
                .toStream()
                .map(this::toAuthorDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void delete(UUID uuid) {
        graph.traversal().V()
                .as(AUTHOR).hasId(uuid.toString())
                .drop().iterate();
        LOGGER.info("Deleted Author with id: {}", uuid);

    }

    private AuthorDto toAuthorDto(Vertex v) {
        return new AuthorDto(UUID.fromString(v.id().toString()), v.property(NAME).value().toString());
    }

}
