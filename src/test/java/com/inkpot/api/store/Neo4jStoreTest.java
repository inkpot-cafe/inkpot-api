package com.inkpot.api.store;

import com.inkpot.core.store.DocumentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.neo4j.driver.Values.parameters;

@SpringBootTest
class Neo4jStoreTest {

    static final UUID RANDOM_UUID = UUID.randomUUID();
    static final String TITLE = "title";
    static final String AUTHOR = "author";
    static final String CONTENT = "content";

    @Mock
    Session session;

    @Mock
    Transaction transaction;

    @Mock
    Result result;

    @MockBean
    Driver driver;

    @Autowired
    Neo4jStore store;

    @BeforeEach
    void setUp() {
        setUpDriver();
        setUpSession();
        setUpTransaction();
    }

    @Test
    void save() {
        // given
        DocumentDto documentDto = new DocumentDto(RANDOM_UUID, AUTHOR, TITLE, CONTENT);

        // when
        store.save(documentDto);

        // then
        verify(transaction).run(
                eq("CREATE (d:Document { uuid: $uuid, title: $title, author: $author, content: $content })"),
                eq(parameters("uuid", RANDOM_UUID.toString(),
                        "author", AUTHOR,
                        "title", TITLE,
                        "content", CONTENT))
        );
    }

    @Test
    void find() {
        // given
        Stream<Record> stream = Stream.of(documentRecord(RANDOM_UUID));
        when(result.stream()).thenReturn(stream);

        // when
        DocumentDto documentDto = store.find(RANDOM_UUID);

        // then
        verify(transaction).run(
                eq("MATCH (d:Document) WHERE d.uuid = $uuid RETURN properties(d)"),
                eq(parameters("uuid", RANDOM_UUID.toString()))
        );

        DocumentDto expected = new DocumentDto(RANDOM_UUID, AUTHOR, TITLE, CONTENT);
        assertThat(documentDto).isEqualTo(expected);
    }

    @Test
    void findAll() {
        // given
        UUID uuid0 = UUID.randomUUID();
        UUID uuid1 = UUID.randomUUID();
        Stream<Record> stream = Stream.of(documentRecord(uuid0), documentRecord(uuid1));
        when(result.stream()).thenReturn(stream);

        // when
        Set<DocumentDto> documentDtoSet = store.findAll();

        // then
        verify(transaction).run(eq("MATCH (d:Document) RETURN properties(d)"));

        DocumentDto expected0 = new DocumentDto(uuid0, AUTHOR, TITLE, CONTENT);
        DocumentDto expected1 = new DocumentDto(uuid1, AUTHOR, TITLE, CONTENT);
        assertThat(documentDtoSet).hasSize(2).contains(expected0, expected1);
    }

    @Test
    void delete() {
        // when
        store.delete(RANDOM_UUID);

        // then
        verify(transaction).run(
                eq("MATCH (d:Document) WHERE d.uuid = $uuid DELETE d"),
                eq(parameters("uuid", RANDOM_UUID.toString()))
        );

        verify(session).close();
    }

    @Test
    void close() {
        store.close();

        verify(driver).close();
    }

    private void setUpDriver() {
        when(driver.session()).thenReturn(session);
    }

    private void setUpTransaction() {
        when(transaction.run(anyString())).thenReturn(result);
        when(transaction.run(anyString(), any(Value.class))).thenReturn(result);
    }

    private void setUpSession() {
        when(session.writeTransaction(any(TransactionWork.class)))
                .then(invocation -> {
                    TransactionWork<Value> work = invocation.getArgument(0);
                    return work.execute(transaction);
                });

        when(session.readTransaction(any(TransactionWork.class)))
                .then(invocation -> {
                    TransactionWork<Value> work = invocation.getArgument(0);
                    return work.execute(transaction);
                });
    }

    private Record documentRecord(UUID uuid) {
        Record record = mock(Record.class);
        Value value = mock(Value.class);

        addValue(value, "uuid", uuid.toString());
        addValue(value, "author", AUTHOR);
        addValue(value, "title", TITLE);
        addValue(value, "content", CONTENT);

        when(record.get(eq(0))).thenReturn(value);
        return record;
    }

    private void addValue(Value value, String key, String str) {
        Value authorValue = mock(Value.class);
        when(authorValue.asString()).thenReturn(str);
        when(value.get(key)).thenReturn(authorValue);
    }

}