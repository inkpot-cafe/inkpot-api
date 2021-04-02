package com.inkpot.api.controller.auth;

import io.quarkus.security.Authenticated;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/auth")
@ApplicationScoped
public class AuthController {

    @POST
    @Path("token")
    @Authenticated
    public String auth() {
        return "token";
    }

}
