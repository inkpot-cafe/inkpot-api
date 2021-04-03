package com.inkpot.api.controller.document;

import com.inkpot.api.iam.Role;
import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.Document;
import com.inkpot.core.application.port.service.DocumentService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@QuarkusTest
class DocumentsControllerTest {

    public static final String DOCUMENTS_ENDPOINT = "/documents";
    public static final UUID AUTHOR_ID_VALUE = UUID.randomUUID();
    public static final String TITLE_VALUE = "title";
    public static final String CONTENT_VALUE = "content";
    public static final String AUTHOR_ID = "authorId";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    @Mock
    DocumentService documentService;

    @InjectMock
    CoreContext coreContext;

    @InjectSpy
    DocumentsController documentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(coreContext.documentService()).thenReturn(documentService);
    }

    @Test
    @TestSecurity(user = "testAuthor", roles = Role.AUTHOR)
    void createDocument() {
        UUID id = UUID.randomUUID();
        when(documentService.createDocument(any())).thenReturn(aDocument(id));

        String json = new JSONObject()
                .put(AUTHOR_ID, AUTHOR_ID_VALUE)
                .put(TITLE, TITLE_VALUE)
                .put(CONTENT, CONTENT_VALUE).toString();
        String body = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(DOCUMENTS_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        JSONObject jsonBody = new JSONObject(body);
        assertThat(jsonBody.get("id")).isEqualTo(id.toString());
        assertThat(jsonBody.get(AUTHOR_ID)).isEqualTo(AUTHOR_ID_VALUE.toString());
        assertThat(jsonBody.get(TITLE)).isEqualTo(TITLE_VALUE);
        assertThat(jsonBody.get(CONTENT)).isEqualTo(CONTENT_VALUE);

        verify(documentService).createDocument(any());
        verify(documentsController).createDocument(any());
    }

    @Test
    void findDocument() {
        UUID id = UUID.randomUUID();
        when(documentService.findDocument(id)).thenReturn(Optional.of(aDocument(id)));

        String body = RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .get(DOCUMENTS_ENDPOINT + "/{id}")
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        JSONObject jsonBody = new JSONObject(body);
        assertThat(jsonBody.get("id")).isEqualTo(id.toString());
        assertThat(jsonBody.get(AUTHOR_ID)).isEqualTo(AUTHOR_ID_VALUE.toString());
        assertThat(jsonBody.get(TITLE)).isEqualTo(TITLE_VALUE);
        assertThat(jsonBody.get(CONTENT)).isEqualTo(CONTENT_VALUE);

        verify(documentService).findDocument(id);
        verify(documentsController).findDocument(eq(id));
    }

    @Test
    void findDocumentNotFound() {
        UUID id = UUID.randomUUID();
        when(documentService.findDocument(id)).thenReturn(Optional.empty());

        RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .get(DOCUMENTS_ENDPOINT + "/{id}")
                .then()
                .assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        verify(documentService).findDocument(id);
        verify(documentsController).findDocument(eq(id));
    }

    @Test
    void findAllDocuments() {
        UUID id = UUID.randomUUID();
        when(documentService.findAllDocuments()).thenReturn(Set.of(aDocument(id)));

        String body = RestAssured
                .given()
                .when()
                .get(DOCUMENTS_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        JSONArray jsonBody = new JSONArray(body);
        assertThat(jsonBody.length()).isEqualTo(1);
        assertThat(jsonBody.getJSONObject(0).get("id")).isEqualTo(id.toString());
        assertThat(jsonBody.getJSONObject(0).get(AUTHOR_ID)).isEqualTo(AUTHOR_ID_VALUE.toString());
        assertThat(jsonBody.getJSONObject(0).get(TITLE)).isEqualTo(TITLE_VALUE);
        assertThat(jsonBody.getJSONObject(0).get(CONTENT)).isEqualTo(CONTENT_VALUE);

        verify(documentService).findAllDocuments();
        verify(documentsController).findAllDocuments();
    }

    @Test
    @TestSecurity(user = "testAuthor", roles = Role.AUTHOR)
    void deleteDocument() {
        UUID id = UUID.randomUUID();
        when(documentService.findDocument(id)).thenReturn(Optional.of(aDocument(id)));

        RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .delete(DOCUMENTS_ENDPOINT + "/{id}")
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode());

        verify(documentService).deleteDocument(id);
        verify(documentsController).deleteDocument(eq(id));
    }

    private Document aDocument(UUID id) {
        return new Document(id, AUTHOR_ID_VALUE, TITLE_VALUE, CONTENT_VALUE);
    }

    @Test
    @TestSecurity(user = "testForbidden", roles = Role.ADMIN)
    void createDocumentForbidden() {
        UUID id = UUID.randomUUID();
        when(documentService.createDocument(any())).thenReturn(aDocument(id));

        String json = new JSONObject()
                .put(AUTHOR_ID, AUTHOR_ID_VALUE)
                .put(TITLE, TITLE_VALUE)
                .put(CONTENT, CONTENT_VALUE).toString();
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(DOCUMENTS_ENDPOINT)
                .then()
                .assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

        verify(documentService, never()).createDocument(any());
        verify(documentsController).createDocument(any());
    }

    @Test
    @TestSecurity(user = "testForbidden", roles = Role.ADMIN)
    void deleteDocumentForbidden() {
        UUID id = UUID.randomUUID();
        when(documentService.findDocument(id)).thenReturn(Optional.of(aDocument(id)));

        RestAssured
                .given()
                .pathParam("id", id)
                .when()
                .delete(DOCUMENTS_ENDPOINT + "/{id}")
                .then()
                .assertThat()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

        verify(documentService, never()).deleteDocument(id);
        verify(documentsController).deleteDocument(eq(id));
    }

}
