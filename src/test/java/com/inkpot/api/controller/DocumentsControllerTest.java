package com.inkpot.api.controller;

import com.inkpot.core.CoreContext;
import com.inkpot.core.domain.Document;
import com.inkpot.core.domain.DocumentFactory;
import com.inkpot.core.domain.DocumentRepository;
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
    DocumentFactory documentFactory;

    @Mock
    DocumentRepository documentRepository;

    @Mock
    CoreContext coreContext;

    @InjectMocks
    DocumentsController documentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
        Response responseEntity = documentsController.createDocument(createDocument(TITLE, AUTHOR, CONTENT));

        // then
        verify(documentRepository).save(document);
        assertThat(responseEntity.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(responseEntity.getEntity()).isEqualTo(document);
    }

    @Test
    void findDocument() {
        // given
        Document document = mock(Document.class);
        when(documentRepository.find(eq(RANDOM_UUID))).thenReturn(document);

        // when
        Response responseEntity = documentsController.findDocument(RANDOM_UUID);

        // then
        verify(documentRepository).find(RANDOM_UUID);
        assertThat(responseEntity.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(responseEntity.getEntity()).isEqualTo(document);
    }

    @Test
    void findDocumentNotFound() {
        // given
        when(documentRepository.find(eq(RANDOM_UUID))).thenReturn(null);

        // when
        Response responseEntity = documentsController.findDocument(RANDOM_UUID);

        // then
        verify(documentRepository).find(RANDOM_UUID);
        assertThat(responseEntity.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void findAllDocuments() {
        // given
        Document document0 = mock(Document.class);
        Document document1 = mock(Document.class);
        when(documentRepository.findAll()).thenReturn(Set.of(document0, document1));

        // when
        Response responseEntity = documentsController.findAllDocuments();

        // then
        verify(documentRepository).findAll();
        assertThat(responseEntity.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat((Set<Document>)responseEntity.getEntity()).hasSize(2).contains(document0, document1);
    }

    @Test
    void deleteDocument() {
        // when
        Response responseEntity = documentsController.deleteDocument(RANDOM_UUID);

        // then
        verify(documentRepository).delete(RANDOM_UUID);
        assertThat(responseEntity.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
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