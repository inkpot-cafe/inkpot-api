package com.inkpot.server;

import com.inkpot.core.Context;
import com.inkpot.core.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class DocumentController {

    @Autowired
    private Context context;

    @PostMapping("/document")
    public UUID createDocument(@RequestBody CreateDocument createDocument) {
        Document document = context.newDocument(createDocument.getTitle(), createDocument.getAuthor());
        document.write(createDocument.getContent());
        context.save(document);
        return document.getUuid();
    }

    @GetMapping("/document")
    public Document readDocument(@RequestParam UUID uuid) {
        return context.load(uuid);
    }

    @DeleteMapping("/document")
    public void deleteDocument(@RequestParam UUID uuid) {
        Document document = context.load(uuid);
        if (document != null) {
            context.delete(document);
        }
    }

}
