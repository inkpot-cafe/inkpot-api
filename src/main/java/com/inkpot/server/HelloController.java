package com.inkpot.server;

import com.inkpot.core.Document;
import com.inkpot.core.Inkpot;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @RequestMapping("/")
    public String index() {
        Document document = Inkpot.INSTANCE.newDocument("title", "author");
        return document.getUuid() + " author: " + document.getAuthor() + " title: " + document.getTitle();
    }
}
