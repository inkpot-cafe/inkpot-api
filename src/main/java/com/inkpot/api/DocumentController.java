package com.inkpot.api;

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
        return context.findAllDocuments();
    }

    @PostMapping("/document")
    public UUID createDocument(@RequestBody CreateDocument createDocument) {
        Document document = context.documentBuilder()
                .title(createDocument.getTitle())
                .author(createDocument.getAuthor())
                .content(createDocument.getContent())
                .build();
        context.saveDocument(document);
        return document.getUuid();
    }

    @GetMapping("/document")
    public Document readDocument(@RequestParam UUID uuid) {
        return context.findDocument(uuid);
    }

    @DeleteMapping("/document")
    public void deleteDocument(@RequestParam UUID uuid) {
        context.deleteDocument(uuid);
    }

}
