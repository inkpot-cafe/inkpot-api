package com.inkpot.api.controller.auth;

import com.inkpot.api.iam.Authenticator;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("auth")
@ApplicationScoped
public class AuthController {

    private final Authenticator authenticator;

    @Inject
    public AuthController(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @GET
    @Path("token")
    public String token() {
        return authenticator.currentAuthenticatedUser()
                .generateToken().asString();
    }

}
