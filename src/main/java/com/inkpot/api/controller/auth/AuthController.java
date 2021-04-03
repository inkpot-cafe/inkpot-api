package com.inkpot.api.controller.auth;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/auth")
@ApplicationScoped
public class AuthController {

    @Inject
    private CurrentIdentityAssociation association;

    @POST
    @Path("token")
    public String auth() {
        var identity = association.getIdentity();

        return identity.getPrincipal().getName();
    }

}
