package com.inkpot.api.store;

import com.inkpot.core.application.port.store.DocumentDto;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
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
        GraphTraversalSource g = tinkerGraphProvider.instantiateGraph();
        g.addV("author").property(T.id, AUTHOR_ID.toString()).iterate();
        tinkerGraphDocumentStore = new TinkerGraphDocumentStore(g);
    }

    @Test
    void testSaveAndFindDocument() {
        UUID id = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(id, AUTHOR_ID, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        Optional<DocumentDto> foundDocument = tinkerGraphDocumentStore.find(id);

        assertThat(foundDocument.isPresent()).isTrue();
        assertThat(foundDocument.get()).isEqualTo(savedDocument);
    }

    @Test
    void testSaveAndFindAllDocuments() {
        UUID id = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(id, AUTHOR_ID, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        Set<DocumentDto> foundDocuments = tinkerGraphDocumentStore.findAll();

        assertThat(foundDocuments).containsOnly(savedDocument);
    }

    @Test
    void testSaveAndDeleteAndFindDocument() {
        UUID id = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(id, AUTHOR_ID, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        tinkerGraphDocumentStore.delete(id);
        restartTinkerGraph();
        Optional<DocumentDto> foundDocument = tinkerGraphDocumentStore.find(id);

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