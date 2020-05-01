package com.inkpot.api.controller;

import com.inkpot.core.CoreContext;
import com.inkpot.core.domain.Document;
import com.inkpot.core.domain.DocumentFactory;
import com.inkpot.core.domain.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
class DocumentControllerTest {

    static final String TITLE = "title";
    static final String AUTHOR = "author";
    static final String CONTENT = "content";
    static final UUID RANDOM_UUID = UUID.randomUUID();

    @Mock
    DocumentFactory documentFactory;

    @Mock
    DocumentRepository documentRepository;

    @MockBean
    CoreContext coreContext;

    @Autowired
    DocumentController documentController;

    @BeforeEach
    void setUp() {
        setUpCoreContext();
    }

    @Test
    void createDocument() throws Exception {
        // given
        Document document = mock(Document.class);
        when(documentFactory.author(anyString())).thenReturn(documentFactory);
        when(documentFactory.content(anyString())).thenReturn(documentFactory);
        when(documentFactory.title(anyString())).thenReturn(documentFactory);
        when(documentFactory.create()).thenReturn(document);

        // when
        ResponseEntity<Document> responseEntity = documentController.createDocument(createDocument(TITLE, AUTHOR, CONTENT));

        // then
        verify(documentRepository).save(document);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(document);
    }

    @Test
    void findDocument() {
        // given
        Document document = mock(Document.class);
        when(documentRepository.find(eq(RANDOM_UUID))).thenReturn(document);

        // when
        ResponseEntity<Document> responseEntity = documentController.findDocument(RANDOM_UUID);

        // then
        verify(documentRepository).find(RANDOM_UUID);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(document);
    }

    @Test
    void findDocumentNotFound() {
        // given
        when(documentRepository.find(eq(RANDOM_UUID))).thenReturn(null);

        // when
        ResponseEntity<Document> responseEntity = documentController.findDocument(RANDOM_UUID);

        // then
        verify(documentRepository).find(RANDOM_UUID);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAllDocuments() {
        // given
        Document document0 = mock(Document.class);
        Document document1 = mock(Document.class);
        when(documentRepository.findAll()).thenReturn(Set.of(document0, document1));

        // when
        ResponseEntity<Set<Document>> responseEntity = documentController.findAllDocuments();

        // then
        verify(documentRepository).findAll();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2).contains(document0, document1);
    }

    @Test
    void deleteDocument() {
        // when
        ResponseEntity<Void> responseEntity = documentController.deleteDocument(RANDOM_UUID);

        // then
        verify(documentRepository).delete(RANDOM_UUID);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void setUpCoreContext() {
        when(coreContext.documentFactory()).thenReturn(documentFactory);
        when(coreContext.documentRepository()).thenReturn(documentRepository);
    }

    private CreateDocument createDocument(String title, String author, String content) {
        var createDocument = new CreateDocument();
        createDocument.setTitle(title);
        createDocument.setAuthor(author);
        createDocument.setContent(content);
        return createDocument;
    }

}