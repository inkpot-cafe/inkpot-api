package com.inkpot.api.store;

import com.inkpot.core.store.DocumentDto;
import com.inkpot.core.store.DocumentStore;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.neo4j.driver.Values.parameters;

@Repository
public class Neo4JStore implements DocumentStore, AutoCloseable {

    private final Driver driver;

    @Autowired
    public Neo4JStore() {
        this.driver = GraphDatabase.driver("bolt://192.168.99.100:7687", AuthTokens.basic("neo4j", "test"));
    }

    @Override
    public void save(@NonNull DocumentDto document) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                tx.run("CREATE (d:Document { uuid: $uuid, title: $title, author: $author, content: $content })",
                        parameters("author", document.getAuthor(),
                                "title", document.getTitle(),
                                "uuid", document.getUuid().toString(),
                                "content", document.getContent()));
                return null;
            });
        }
    }

    @Override
    public DocumentDto find(@NonNull UUID uuid) {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                Result d = tx.run("MATCH (d:Document) " +
                                "WHERE d.uuid = $uuid " +
                                "RETURN properties(d)",
                        parameters("uuid", uuid.toString()));

                return toDocumentDto(d.single().get(0));
            });
        }
    }

    @Override
    public void delete(@NonNull UUID uuid) {

    }

    @Override
    @NonNull
    public Set<DocumentDto> findAll() {
        try (Session session = driver.session()) {
            return session.readTransaction(tx -> {
                Result d = tx.run("MATCH (d:Document) RETURN properties(d)");

                Set<DocumentDto> documents = new HashSet<>();
                for (Record r : d.list()) {
                    documents.add(toDocumentDto(r.get(0)));
                }
                return documents;

            });
        }

    }

    @Override
    public void close() {
        driver.close();
    }

    private DocumentDto toDocumentDto(Value value) {
        return new DocumentDto(UUID.fromString(value.get("uuid").asString()),
                value.get("author").asString(),
                value.get("title").asString(),
                value.get("content").asString());
    }
}
