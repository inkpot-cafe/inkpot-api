package com.inkpot.api.controller;

import com.inkpot.core.CoreContext;
import com.inkpot.core.domain.Document;
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
    public ResponseEntity<Document> createDocument(@RequestBody CreateDocument createDocument) {
        var document = context.documentFactory()
                .title(createDocument.getTitle())
                .author(createDocument.getAuthor())
                .content(createDocument.getContent())
                .create();
        context.documentRepository().save(document);
        return ResponseEntity.ok(document);
    }

    @GetMapping(
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Document> findDocument(@PathVariable UUID uuid) {
        Optional<Document> document = Optional.ofNullable(context.documentRepository().find(uuid));
        return ResponseEntity.of(document);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Set<Document>>findAllDocuments() {
        return ResponseEntity.ok(context.documentRepository().findAll());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID uuid) {
        context.documentRepository().delete(uuid);
        return ResponseEntity.ok().build();
    }

}
