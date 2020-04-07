package com.inkpot.server;

import com.inkpot.core.CoreContext;
import com.inkpot.core.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
public class DocumentController {

    @Autowired
    private CoreContext context;

    @GetMapping("/documents")
    public Set<Document> readAllDocuments() {
        return context.readAllDocuments();
    }

    @PostMapping("/document")
    public UUID createDocument(@RequestBody CreateDocument createDocument) {
        Document document = context.newDocument(createDocument.getTitle(), createDocument.getAuthor());
        document.write(createDocument.getContent());
        context.saveDocument(document);
        return document.getUuid();
    }

    @GetMapping("/document")
    public Document readDocument(@RequestParam UUID uuid) {
        return context.readDocument(uuid);
    }

    @DeleteMapping("/document")
    public void deleteDocument(@RequestParam UUID uuid) {
        Document document = context.readDocument(uuid);
        if (document != null) {
            context.deleteDocument(document);
        }
    }

}
