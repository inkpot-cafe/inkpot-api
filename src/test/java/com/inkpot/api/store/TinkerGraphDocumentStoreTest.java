package com.inkpot.api.store;

import com.inkpot.core.application.port.store.DocumentDto;
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
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    private TinkerGraphDocumentStore tinkerGraphDocumentStore;

    @BeforeEach
    void setUp() throws IOException {
        cleanTinkerGraphData();
        tinkerGraphDocumentStore = new TinkerGraphDocumentStore(Optional.of(GRAPH_LOCATION));
        tinkerGraphDocumentStore.startUp();
    }

    @Test
    void testTearDownPersist() {
        tinkerGraphDocumentStore.tearDown();

        assertThat(Files.exists(Path.of(GRAPH_LOCATION))).isTrue();
    }

    @Test
    void testSaveAndFindDocument() {
        UUID uuid = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(uuid, AUTHOR, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        Optional<DocumentDto> foundDocument = tinkerGraphDocumentStore.find(uuid);

        assertThat(foundDocument.isPresent()).isTrue();
        assertThat(foundDocument.get()).isEqualTo(savedDocument);
    }

    @Test
    void testSaveAndFindAllDocuments() {
        UUID uuid = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(uuid, AUTHOR, TITLE, CONTENT);

        tinkerGraphDocumentStore.save(savedDocument);
        restartTinkerGraph();
        Set<DocumentDto> foundDocuments = tinkerGraphDocumentStore.findAll();

        assertThat(foundDocuments).containsOnly(savedDocument);
    }

    @Test
    void testSaveAndDeleteAndFindDocument() {
        UUID uuid = UUID.randomUUID();
        DocumentDto savedDocument = new DocumentDto(uuid, AUTHOR, TITLE, CONTENT);

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
        tinkerGraphDocumentStore.tearDown();
        tinkerGraphDocumentStore.startUp();
    }

}