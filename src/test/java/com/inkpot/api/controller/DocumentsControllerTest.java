package com.inkpot.api.controller;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.Document;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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