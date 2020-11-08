package com.inkpot.api.controller;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class DocumentsControllerTest {

    static final String TITLE = "title";
    static final String AUTHOR = "author";
    static final String CONTENT = "content";
    static final UUID RANDOM_UUID = UUID.randomUUID();

    @MockBean
    CoreContext coreContext;

    @Autowired
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