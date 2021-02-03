package com.inkpot.api.store;

import com.inkpot.core.store.DocumentDto;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.neo4j.driver.Record;
import org.neo4j.driver.*;

import javax.inject.Inject;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.neo4j.driver.Values.parameters;

@QuarkusTest
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

    @Mock
    Driver driver;

    @InjectMocks
    Neo4jStore store;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
                eq(Neo4jStore.CREATE_DOCUMENT),
                eq(parameters(Neo4jStore.UUID_PARAMETER, RANDOM_UUID.toString(),
                        Neo4jStore.AUTHOR_PARAMETER, AUTHOR,
                        Neo4jStore.TITLE_PARAMETER, TITLE,
                        Neo4jStore.CONTENT_PARAMETER, CONTENT))
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
                eq(Neo4jStore.FIND_DOCUMENT),
                eq(parameters(Neo4jStore.UUID_PARAMETER, RANDOM_UUID.toString()))
        );

        DocumentDto expected = new DocumentDto(RANDOM_UUID, AUTHOR, TITLE, CONTENT);
        assertThat(documentDto).isEqualTo(expected);
    }

    @Test
    void findNoResults() {
        // given
        Stream<Record> stream = Stream.empty();
        when(result.stream()).thenReturn(stream);

        // when
        DocumentDto documentDto = store.find(RANDOM_UUID);

        // then
        verify(transaction).run(
                eq(Neo4jStore.FIND_DOCUMENT),
                eq(parameters(Neo4jStore.UUID_PARAMETER, RANDOM_UUID.toString()))
        );

        DocumentDto expected = new DocumentDto(RANDOM_UUID, AUTHOR, TITLE, CONTENT);
        assertThat(documentDto).isNull();
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
        verify(transaction).run(eq(Neo4jStore.FIND_ALL_DOCUMENTS));

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
                eq(Neo4jStore.DELETE_DOCUMENT),
                eq(parameters(Neo4jStore.UUID_PARAMETER, RANDOM_UUID.toString()))
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
                    TransactionWork<Value> work = (TransactionWork<Value>)invocation.getArguments()[0];
                    return work.execute(transaction);
                });

        when(session.readTransaction(any(TransactionWork.class)))
                .then(invocation -> {
                    TransactionWork<Value> work = (TransactionWork<Value>)invocation.getArguments()[0];
                    return work.execute(transaction);
                });
    }

    private Record documentRecord(UUID uuid) {
        Record record = mock(Record.class);
        Value value = mock(Value.class);

        addValue(value, Neo4jStore.UUID_PARAMETER, uuid.toString());
        addValue(value, Neo4jStore.AUTHOR_PARAMETER, AUTHOR);
        addValue(value, Neo4jStore.TITLE_PARAMETER, TITLE);
        addValue(value, Neo4jStore.CONTENT_PARAMETER, CONTENT);

        when(record.get(eq(0))).thenReturn(value);
        return record;
    }

    private void addValue(Value value, String key, String str) {
        Value authorValue = mock(Value.class);
        when(authorValue.asString()).thenReturn(str);
        when(value.get(key)).thenReturn(authorValue);
    }

}