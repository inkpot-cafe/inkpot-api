package com.inkpot.api.controller;

import com.inkpot.api.controller.request.CreateDocumentRequest;
import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.CreateDocument;
import com.inkpot.core.application.port.service.Document;

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
    public Response createDocument(CreateDocumentRequest request) {
        Document document = context.documentService().createDocument(toCreateDocument(request));
        return Response.ok(document).build();
    }

    @GET
    @Path("/{uuid}")
    public Response findDocument(@PathParam("uuid") UUID uuid) {
        Optional<Document> document = Optional.ofNullable(context.documentService().findDocument(uuid));
        return document.map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    public Response findAllDocuments() {
        return Response.ok(context.documentService().findAllDocuments()).build();
    }

    @DELETE
    @Path("/{uuid}")
    public Response deleteDocument(@PathParam("uuid") UUID uuid) {
        context.documentService().deleteDocument(uuid);
        return Response.ok().build();
    }

    private CreateDocument toCreateDocument(CreateDocumentRequest request) {
        return new CreateDocument(
                request.getAuthor(),
                request.getTitle(),
                request.getContent()
        );
    }
}
