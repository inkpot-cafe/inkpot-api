package com.inkpot.api.controller;

import com.inkpot.core.application.CoreContext;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

@QuarkusTest
class DocumentsControllerTest {

    static final String TITLE = "title";
    static final String AUTHOR = "author";
    static final String CONTENT = "content";
    static final UUID RANDOM_UUID = UUID.randomUUID();

    @Mock
    CoreContext coreContext;

    @InjectMocks
    DocumentsController documentsController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createDocument() throws Exception {

    }

    @Test
    void findDocument() {

    }

    @Test
    void findDocumentNotFound() {

    }

    @Test
    void findAllDocuments() {

    }

    @Test
    void deleteDocument() {

    }


}