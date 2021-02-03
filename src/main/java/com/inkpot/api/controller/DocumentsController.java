package com.inkpot.api.controller;

import com.inkpot.core.CoreContext;
import com.inkpot.core.domain.Document;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("documents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DocumentsController {

    private final CoreContext context;

    @Inject
    public DocumentsController(CoreContext context) {
        this.context = context;
    }

    @POST
    public Response createDocument(CreateDocument createDocument) {
        var document = context.documentFactory()
                .title(createDocument.getTitle())
                .author(createDocument.getAuthor())
                .content(createDocument.getContent())
                .create();
        context.documentRepository().save(document);
        return Response.ok(document).build();
    }

    @GET
    @Path("/{uuid}")
    public Response findDocument(@PathParam("uuid") UUID uuid) {
        Optional<Document> document = Optional.ofNullable(context.documentRepository().find(uuid));
        return document.map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    public Response findAllDocuments() {
        return Response.ok(context.documentRepository().findAll()).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteDocument(@PathParam("uuid") UUID uuid) {
        context.documentRepository().delete(uuid);
        return Response.ok().build();
    }

}
