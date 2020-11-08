package com.inkpot.api.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.UUID;

class Neo4jStoreTest {

    static final UUID RANDOM_UUID = UUID.randomUUID();
    static final String TITLE = "title";
    static final String AUTHOR = "author";
    static final String CONTENT = "content";

    @SpyBean
    Driver driver;

    @Autowired
    Neo4jStore store;

    @BeforeEach
    void setUp() {

    }

    @Test
    void save() {

    }

    @Test
    void find() {

    }

    @Test
    void findNoResults() {

    }

    @Test
    void findAll() {

    }

    @Test
    void delete() {

    }

    @Test
    void close() {

    }

}