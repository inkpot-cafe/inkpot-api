package com.inkpot.api.controller.author;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.Author;
import com.inkpot.core.application.port.service.AuthorCreateData;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class AuthorController {

    private final CoreContext coreContext;

    @Inject
    public AuthorController(CoreContext coreContext) {
        this.coreContext = coreContext;
    }

    @POST
    public Response createAuthor(AuthorCreateRequest request) {
        Author author = coreContext.authorService().createAuthor(new AuthorCreateData(request.getName()));
        return Response.ok(author).build();
    }

    @GET
    @Path("/{uuid}")
    public Response findAuthor(@PathParam("uuid") UUID uuid) {
        Optional<Author> author = coreContext.authorService().findAuthor(uuid);
        return author.map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    public Response findAllAuthors() {
        return Response.ok(coreContext.authorService().findAllAuthors()).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteAuthor(@PathParam("uuid") UUID uuid) {
        coreContext.authorService().deleteAuthor(uuid);
        return Response.ok().build();
    }

}