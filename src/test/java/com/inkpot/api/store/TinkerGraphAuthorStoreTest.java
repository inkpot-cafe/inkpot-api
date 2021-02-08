package com.inkpot.api.store;

import com.inkpot.core.application.port.store.AuthorDto;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TinkerGraphAuthorStoreTest {

    public static final String GRAPH_LOCATION = "graph.kryo";
    public static final String NAME = "name";
    private TinkerGraphAuthorStore tinkerGraphAuthorStore;
    private TinkerGraphProvider tinkerGraphProvider;

    @BeforeEach
    void setUp() throws IOException {
        cleanTinkerGraphData();
        tinkerGraphProvider = new TinkerGraphProvider(Optional.of(GRAPH_LOCATION));
        Graph graph = tinkerGraphProvider.instantiateGraph();
        tinkerGraphAuthorStore = new TinkerGraphAuthorStore(graph);
    }

    @Test
    void testSaveAndFindDocument() {
        UUID uuid = UUID.randomUUID();
        AuthorDto savedAuthor = new AuthorDto(uuid, NAME);

        tinkerGraphAuthorStore.save(savedAuthor);
        restartTinkerGraph();
        Optional<AuthorDto> foundAuthor = tinkerGraphAuthorStore.find(uuid);

        assertThat(foundAuthor.isPresent()).isTrue();
        assertThat(foundAuthor.get()).isEqualTo(savedAuthor);
    }

    @Test
    void testSaveAndFindAllDocuments() {
        UUID uuid = UUID.randomUUID();
        AuthorDto savedAuthor = new AuthorDto(uuid, NAME);

        tinkerGraphAuthorStore.save(savedAuthor);
        restartTinkerGraph();
        Set<AuthorDto> foundAuthors = tinkerGraphAuthorStore.findAll();

        assertThat(foundAuthors).containsOnly(savedAuthor);
    }

    @Test
    void testSaveAndDeleteAndFindDocument() {
        UUID uuid = UUID.randomUUID();
        AuthorDto savedAuthor = new AuthorDto(uuid, NAME);

        tinkerGraphAuthorStore.save(savedAuthor);
        restartTinkerGraph();
        tinkerGraphAuthorStore.delete(uuid);
        restartTinkerGraph();
        Optional<AuthorDto> foundAuthor = tinkerGraphAuthorStore.find(uuid);

        assertThat(foundAuthor.isPresent()).isFalse();
    }

    @AfterAll
    static void afterAll() throws IOException {
        cleanTinkerGraphData();
    }

    private static void cleanTinkerGraphData() throws IOException {
        Files.deleteIfExists(Path.of(GRAPH_LOCATION));
    }

    private void restartTinkerGraph() {
        tinkerGraphProvider.tearDown();
        tinkerGraphProvider.instantiateGraph();
    }

}