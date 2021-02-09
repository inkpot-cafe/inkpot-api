package com.inkpot.api.store;

import com.inkpot.core.application.port.store.DocumentDto;
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

class TinkerGraphDocumentStoreTest {

    public static final String GRAPH_LOCATION = "graph.kryo";
    public static final UUID AUTHOR_ID = UUID.randomUUID();
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    private TinkerGraphDocumentStore tinkerGraphDocumentStore;
    private TinkerGraphProvider tinkerGraphProvider;

    @BeforeEach
    void setUp() throws IOException {
        cleanTinkerGraphData();
        tinkerGraphProvider = new TinkerGraphProvider(Optional.of(GRAPH_LOCATION));
        Graph graph = tinkerGraphProvider.instantiateGraph();
        tinkerGraphDocumentStore = new TinkerGraphDocumentStore(graph);
    }

    @Test
    void testSaveAndFindDocument() {
        UUID id = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(id, authorId, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        Optional<DocumentDto> foundDocument = tinkerGraphDocumentStore.find(id);

        assertThat(foundDocument.isPresent()).isTrue();
        assertThat(foundDocument.get()).isEqualTo(savedDocument);
    }

    @Test
    void testSaveAndFindAllDocuments() {
        UUID uuid = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(uuid, AUTHOR_ID, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        Set<DocumentDto> foundDocuments = tinkerGraphDocumentStore.findAll();

        assertThat(foundDocuments).containsOnly(savedDocument);
    }

    @Test
    void testSaveAndDeleteAndFindDocument() {
        UUID uuid = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(uuid, AUTHOR_ID, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        tinkerGraphDocumentStore.delete(uuid);
        restartTinkerGraph();
        Optional<DocumentDto> foundDocument = tinkerGraphDocumentStore.find(uuid);

        assertThat(foundDocument.isPresent()).isFalse();
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