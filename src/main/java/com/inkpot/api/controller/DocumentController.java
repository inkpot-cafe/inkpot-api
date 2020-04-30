package com.inkpot.api.controller;

import com.inkpot.core.CoreContext;
import com.inkpot.core.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
public class DocumentController {

    private final CoreContext context;

    @Autowired
    public DocumentController(CoreContext context) {
        this.context = context;
    }

    @GetMapping("/documents")
    public Set<Document> readAllDocuments() {
        return context.documentRepository().findAll();
    }

    @PostMapping("/document")
    public UUID createDocument(@RequestBody CreateDocument createDocument) {
        var document = context.documentBuilder()
                .title(createDocument.getTitle())
                .author(createDocument.getAuthor())
                .content(createDocument.getContent())
                .build();
        context.documentRepository().save(document);
        return document.getUuid();
    }

    @GetMapping("/document")
    public Document readDocument(@RequestParam UUID uuid) {
        return context.documentRepository().find(uuid);
    }

    @DeleteMapping("/document")
    public void deleteDocument(@RequestParam UUID uuid) {
        context.documentRepository().delete(uuid);
    }

}
