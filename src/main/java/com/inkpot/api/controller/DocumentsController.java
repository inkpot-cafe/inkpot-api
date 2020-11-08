package com.inkpot.api.controller;

import com.inkpot.api.controller.request.CreateDocumentRequest;
import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.port.service.CreateDocument;
import com.inkpot.core.application.port.service.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("documents")
public class DocumentsController {

    private final CoreContext context;

    @Autowired
    public DocumentsController(CoreContext context) {
        this.context = context;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Document> createDocument(@RequestBody CreateDocumentRequest request) {
        Document document = context.documentService().createDocument(toCreateDocument(request));
        return ResponseEntity.ok(document);
    }

    @GetMapping(
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Document> findDocument(@PathVariable UUID uuid) {
        Optional<Document> document = Optional.ofNullable(context.documentService().findDocument(uuid));
        return ResponseEntity.of(document);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Set<Document>> findAllDocuments() {
        return ResponseEntity.ok(context.documentService().findAllDocuments());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID uuid) {
        context.documentService().deleteDocument(uuid);
        return ResponseEntity.ok().build();
    }

    private CreateDocument toCreateDocument(CreateDocumentRequest request) {
        return new CreateDocument(
                request.getAuthor(),
                request.getTitle(),
                request.getContent()
        );
    }
}
