package com.inkpot.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inkpot.core.Document;
import com.inkpot.core.Store;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DocumentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @SpyBean
    private Store store;

    @Test
    public void createDocument() throws Exception {
        CreateDocument createDocument = new CreateDocument();
        createDocument.setAuthor("author");
        createDocument.setTitle("title");
        createDocument.setContent("content");

        mvc.perform(post("/document")
                .content(mapper.writeValueAsString(createDocument))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Document> documentCaptor = ArgumentCaptor.forClass(Document.class);
        verify(store).save(documentCaptor.capture());

        Document createdDocument = documentCaptor.getValue();
        assertThat(createdDocument.getAuthor(), equalTo("author"));
        assertThat(createdDocument.getTitle(), equalTo("title"));
        assertThat(createdDocument.read(), equalTo("content"));
    }

    @Test
    void readDocument() throws Exception {
        UUID uuid = UUID.randomUUID();
        Document document = mock(Document.class);
        when(store.load(eq(uuid))).thenReturn(document);

        mvc.perform(get("/document")
                .param("uuid", uuid.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(store).load(eq(uuid));
    }

    @Test
    void deleteDocument() throws Exception {
        UUID uuid = UUID.randomUUID();
        Document document = mock(Document.class);
        when(document.getUuid()).thenReturn(uuid);
        when(store.load(eq(uuid))).thenReturn(document);

        mvc.perform(delete("/document")
                .param("uuid", uuid.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(store).delete(eq(uuid));
        // TODO context should delete uuid and not document

    }
}
