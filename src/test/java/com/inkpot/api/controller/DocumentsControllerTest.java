package com.inkpot.api.controller;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.Document;
import com.inkpot.core.application.port.service.DocumentService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.junit.mockito.InjectSpy;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.config.ConfigProvider;
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

    public static final String DOCUMENTS_ENDPOINT = ConfigProvider.getConfig().getValue("quarkus.resteasy.path", String.class) + "documents";
    public static final String AUTHOR_VALUE = "author";
    public static final String TITLE_VALUE = "title";
    public static final String CONTENT_VALUE = "content";

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
    void createDocument() {
        UUID uuid = UUID.randomUUID();
        when(documentService.createDocument(any())).thenReturn(aDocument(uuid));

        String json = new JSONObject()
                .put("author", AUTHOR_VALUE)
                .put("title", TITLE_VALUE)
                .put("content", CONTENT_VALUE).toString();
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
        assertThat(jsonBody.get("uuid")).isEqualTo(uuid.toString());
        assertThat(jsonBody.get("author")).isEqualTo(AUTHOR_VALUE);
        assertThat(jsonBody.get("title")).isEqualTo(TITLE_VALUE);
        assertThat(jsonBody.get("content")).isEqualTo(CONTENT_VALUE);

        verify(documentService).createDocument(any());
        verify(documentsController).createDocument(any());
    }

    @Test
    void findDocument() {
        UUID uuid = UUID.randomUUID();
        when(documentService.findDocument(uuid)).thenReturn(Optional.of(aDocument(uuid)));

        String body = RestAssured
                .given()
                .pathParam("uuid", uuid)
                .when()
                .get(DOCUMENTS_ENDPOINT + "/{uuid}")
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        JSONObject jsonBody = new JSONObject(body);
        assertThat(jsonBody.get("uuid")).isEqualTo(uuid.toString());
        assertThat(jsonBody.get("author")).isEqualTo(AUTHOR_VALUE);
        assertThat(jsonBody.get("title")).isEqualTo(TITLE_VALUE);
        assertThat(jsonBody.get("content")).isEqualTo(CONTENT_VALUE);

        verify(documentService).findDocument(uuid);
        verify(documentsController).findDocument(eq(uuid));
    }

    @Test
    void findDocumentNotFound() {
        UUID uuid = UUID.randomUUID();
        when(documentService.findDocument(uuid)).thenReturn(Optional.empty());

        RestAssured
                .given()
                .pathParam("uuid", uuid)
                .when()
                .get(DOCUMENTS_ENDPOINT + "/{uuid}")
                .then()
                .assertThat()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        verify(documentService).findDocument(uuid);
        verify(documentsController).findDocument(eq(uuid));
    }

    @Test
    void findAllDocuments() {
        UUID uuid = UUID.randomUUID();
        when(documentService.findAllDocuments()).thenReturn(Set.of(aDocument(uuid)));

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
        assertThat(jsonBody.getJSONObject(0).get("uuid")).isEqualTo(uuid.toString());
        assertThat(jsonBody.getJSONObject(0).get("author")).isEqualTo(AUTHOR_VALUE);
        assertThat(jsonBody.getJSONObject(0).get("title")).isEqualTo(TITLE_VALUE);
        assertThat(jsonBody.getJSONObject(0).get("content")).isEqualTo(CONTENT_VALUE);

        verify(documentService).findAllDocuments();
        verify(documentsController).findAllDocuments();
    }

    @Test
    void deleteDocument() {
        UUID uuid = UUID.randomUUID();
        when(documentService.findDocument(uuid)).thenReturn(Optional.of(aDocument(uuid)));

        RestAssured
                .given()
                .pathParam("uuid", uuid)
                .when()
                .delete(DOCUMENTS_ENDPOINT + "/{uuid}")
                .then()
                .assertThat()
                .statusCode(Response.Status.OK.getStatusCode());

        verify(documentService).deleteDocument(uuid);
        verify(documentsController).deleteDocument(eq(uuid));
    }

    private Document aDocument(UUID uuid) {
        return new Document(uuid, AUTHOR_VALUE, TITLE_VALUE, CONTENT_VALUE);
    }

}