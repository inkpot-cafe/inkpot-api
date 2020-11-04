package com.inkpot.api.store;

import com.inkpot.core.store.DocumentDto;
import com.inkpot.core.store.DocumentStore;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.neo4j.driver.Values.parameters;

@Repository
public class Neo4jStore implements DocumentStore, AutoCloseable {

    public static final String CREATE_DOCUMENT = "CREATE (d:Document { uuid: $uuid, title: $title, author: $author, content: $content })";
    public static final String FIND_DOCUMENT = "MATCH (d:Document) WHERE d.uuid = $uuid RETURN properties(d)";
    public static final String FIND_ALL_DOCUMENTS = "MATCH (d:Document) RETURN properties(d)";
    public static final String DELETE_DOCUMENT = "MATCH (d:Document) WHERE d.uuid = $uuid DELETE d";
    public static final String UUID_PARAMETER = "uuid";
    public static final String AUTHOR_PARAMETER = "author";
    public static final String TITLE_PARAMETER = "title";
    public static final String CONTENT_PARAMETER = "content";

    private final Driver driver;

    @Autowired
    public Neo4jStore(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void save(@NonNull DocumentDto document) {
        writeTransaction(
                CREATE_DOCUMENT,
                parameters(UUID_PARAMETER, document.getUuid().toString(),
                        AUTHOR_PARAMETER, document.getAuthor(),
                        TITLE_PARAMETER, document.getTitle(),
                        CONTENT_PARAMETER, document.getContent())
        );
    }

    @Override
    public DocumentDto find(@NonNull UUID uuid) {
        return readTransaction(
                FIND_DOCUMENT,
                parameters(UUID_PARAMETER, uuid.toString()),
                result -> result.stream()
                        .findFirst()
                        .map(this::toDocumentDto)
                        .orElse(null)
        );
    }

    @NonNull
    @Override
    public Set<DocumentDto> findAll() {
        return readTransaction(
                FIND_ALL_DOCUMENTS,
                result -> result.stream()
                        .map(this::toDocumentDto)
                        .collect(Collectors.toSet())
        );
    }

    @Override
    public void delete(@NonNull UUID uuid) {
        writeTransaction(
                DELETE_DOCUMENT,
                parameters(UUID_PARAMETER, uuid.toString())
        );
    }

    @Override
    public void close() {
        driver.close();
    }

    private void writeTransaction(String query, Value parameters) {
        sessionWork(session -> session.writeTransaction(tx -> tx.run(query, parameters)));
    }

    private <R> R readTransaction(String query, Function<Result, R> returnFunc) {
        return readTransaction(tx -> tx.run(query), returnFunc);
    }

    private <R> R readTransaction(String query, Value parameters, Function<Result, R> returnFunc) {
        return readTransaction(tx -> tx.run(query, parameters), returnFunc);
    }

    private <R> R readTransaction(Function<Transaction, Result> workFunc, Function<Result, R> returnFunc) {
        return sessionWork(session ->
                session.readTransaction(
                        tx -> {
                            var result = workFunc.apply(tx);
                            return returnFunc.apply(result);
                        }));
    }

    private <R> R sessionWork(Function<Session, R> sessionFunc) {
        try (Session session = driver.session()) {
            return sessionFunc.apply(session);
        }
    }

    private DocumentDto toDocumentDto(Record record) {
        Value value = record.get(0);
        return new DocumentDto(UUID.fromString(value.get("uuid").asString()),
                value.get("author").asString(),
                value.get("title").asString(),
                value.get("content").asString());
    }

}
