package com.inkpot.api.controller.auth;

import com.inkpot.api.iam.Authenticator;
import com.inkpot.api.iam.Token;
import com.inkpot.api.iam.User;
import com.inkpot.api.iam.UserTest;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthControllerTest {

    private static final String AUTH_TOKEN_ENDPOINT = "/auth/token";
    private static final User USER = UserTest.buildUser();

    @InjectMock
    Authenticator authenticator;

    @Test
    void authTokenUnauthorized() {
        RestAssured
                .get(AUTH_TOKEN_ENDPOINT)
                .then()
                .statusCode(Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @TestSecurity(user = "test")
    void generateToken() {
        when(authenticator.currentAuthenticatedUser()).thenReturn(USER);

        var token = RestAssured
                .get(AUTH_TOKEN_ENDPOINT)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .extract().body().asString();

        assertThat(Token.isValidStringToken(token)).isTrue();
    }
}
