package com.inkpot.api.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TinkerGraphProviderTest {
    public static final String GRAPH_LOCATION = "graph.kryo";

    private TinkerGraphProvider tinkerGraphProvider;

    @BeforeEach
    void setUp() throws IOException {
        cleanTinkerGraphData();
        tinkerGraphProvider = new TinkerGraphProvider(Optional.of(GRAPH_LOCATION));
        tinkerGraphProvider.instantiateGraph();
    }

    @Test
    void testTearDownPersist() {
        tinkerGraphProvider.tearDown();
        assertThat(Files.exists(Path.of(GRAPH_LOCATION))).isTrue();
    }

    @AfterAll
    static void afterAll() throws IOException {
        cleanTinkerGraphData();
    }

    private static void cleanTinkerGraphData() throws IOException {
        Files.deleteIfExists(Path.of(GRAPH_LOCATION));
    }
}
