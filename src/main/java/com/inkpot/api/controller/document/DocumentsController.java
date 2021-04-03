package com.inkpot.api.controller.document;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.Document;
import com.inkpot.core.application.port.service.DocumentCreateData;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.security.Authenticated;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.UUID;

@Path("/documents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RegisterForReflection(targets = {Document.class})
public class DocumentsController {

    private final CoreContext context;

    @Inject
    public DocumentsController(CoreContext context) {
        this.context = context;
    }

    @POST
    public Response createDocument(DocumentCreateRequest request) {
        Document document = context.documentService().createDocument(toDocumentCreateData(request));
        return Response.ok(document).build();
    }

    @GET
    @Path("/{id}")
    public Response findDocument(@PathParam("id") UUID uuid) {
        Optional<Document> document = context.documentService().findDocument(uuid);
        return document.map(Response::ok).orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }

    @GET
    public Response findAllDocuments() {
        return Response.ok(context.documentService().findAllDocuments()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDocument(@PathParam("id") UUID uuid) {
        context.documentService().deleteDocument(uuid);
        return Response.ok().build();
    }

    private DocumentCreateData toDocumentCreateData(DocumentCreateRequest request) {
        return new DocumentCreateData(
                request.getAuthorId(),
                request.getTitle(),
                request.getContent()
        );
    }

}
