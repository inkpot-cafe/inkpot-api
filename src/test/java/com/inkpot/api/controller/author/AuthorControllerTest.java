package com.inkpot.api.controller.author;

import com.inkpot.api.iam.Role;
import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.Author;
import com.inkpot.core.application.port.service.AuthorService;
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
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@QuarkusTest
class AuthorControllerTest {

    public static final String AUTHOR_ENDPOINT = "/authors";
    public static final String NAME_VALUE = "name";
    public static final String NAME_FIELD = "name";
    public static final String UUID_FIELD = "id";


    @Mock
    AuthorService authorService;

    @InjectMock
    CoreContext coreContext;

    @InjectSpy
    AuthorController authorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(coreContext.authorService()).thenReturn(authorService);
    }

    @Test
    @TestSecurity(user = "testAdmin", roles = Role.ADMIN)
    void createAuthor() {
        UUID uuid = UUID.randomUUID();
        when(authorService.createAuthor(any())).thenReturn(anAuthor(uuid));

        String json = new JSONObject().put(NAME_FIELD, NAME_VALUE).toString();
        String body = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(AUTHOR_ENDPOINT)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        JSONObject jsonResponse = new JSONObject(body);
        assertThat(body).isNotNull();
        assertThat(jsonResponse.get(UUID_FIELD)).isEqualTo(uuid.toString());
        assertThat(jsonResponse.get(NAME_FIELD)).isEqualTo(NAME_VALUE);

        verify(authorService).createAuthor(any());
        verify(authorController).createAuthor(any());
    }

    @Test
    void findAuthor() {
        UUID uuid = UUID.randomUUID();
        when(authorService.findAuthor(uuid)).thenReturn(Optional.of(anAuthor(uuid)));

        String body = RestAssured
                .given()
                .pathParam("id", uuid)
                .when()
                .get(AUTHOR_ENDPOINT + "/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        JSONObject jsonResponse = new JSONObject(body);
        assertThat(body).isNotNull();
        assertThat(jsonResponse.get(UUID_FIELD)).isEqualTo(uuid.toString());
        assertThat(jsonResponse.get(NAME_FIELD)).isEqualTo(NAME_VALUE);

        verify(authorService).findAuthor(uuid);
        verify(authorController).findAuthor(uuid);
    }

    @Test
    void findAuthorNotFound() {
        UUID uuid = UUID.randomUUID();
        when(authorService.findAuthor(uuid)).thenReturn(Optional.empty());

        RestAssured
                .given()
                .pathParam("id", uuid)
                .when()
                .get(AUTHOR_ENDPOINT + "/{id}")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

        verify(authorService).findAuthor(uuid);
        verify(authorController).findAuthor(uuid);
    }

    @Test
    @TestSecurity(user = "testAdmin", roles = Role.ADMIN)
    void findAllAuthors() {
        UUID uuid = UUID.randomUUID();
        when(authorService.findAllAuthors()).thenReturn(Set.of(anAuthor(uuid)));

        String body = RestAssured
                .given()
                .when()
                .get(AUTHOR_ENDPOINT)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        JSONArray jsonResponse = new JSONArray(body);
        assertThat(body).isNotNull();
        assertThat(jsonResponse.length()).isEqualTo(1);
        assertThat(jsonResponse.getJSONObject(0).get(UUID_FIELD)).isEqualTo(uuid.toString());
        assertThat(jsonResponse.getJSONObject(0).get(NAME_FIELD)).isEqualTo(NAME_VALUE);

        verify(authorService).findAllAuthors();
        verify(authorController).findAllAuthors();
    }

    @Test
    @TestSecurity(user = "testAdmin", roles = Role.ADMIN)
    void deleteAuthor() {
        UUID uuid = UUID.randomUUID();

        RestAssured
                .given()
                .pathParam("id", uuid)
                .when()
                .delete(AUTHOR_ENDPOINT + "/{id}")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());


        verify(authorService).deleteAuthor(uuid);
        verify(authorController).deleteAuthor(uuid);
    }

    private Author anAuthor(UUID uuid) {
        return new Author(uuid, NAME_VALUE, Collections.emptySet());
    }

    @Test
    @TestSecurity(user = "testForbidden", roles = Role.AUTHOR)
    void createAuthorForbidden() {
        UUID uuid = UUID.randomUUID();
        when(authorService.createAuthor(any())).thenReturn(anAuthor(uuid));

        String json = new JSONObject().put(NAME_FIELD, NAME_VALUE).toString();
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post(AUTHOR_ENDPOINT)
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());

        verify(authorService, never()).createAuthor(any());
        verify(authorController).createAuthor(any());
    }

    @Test
    @TestSecurity(user = "testForbidden", roles = Role.AUTHOR)
    void deleteAuthorForbidden() {
        UUID uuid = UUID.randomUUID();

        RestAssured
                .given()
                .pathParam("id", uuid)
                .when()
                .delete(AUTHOR_ENDPOINT + "/{id}")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());


        verify(authorService, never()).deleteAuthor(uuid);
        verify(authorController).deleteAuthor(uuid);
    }
}
