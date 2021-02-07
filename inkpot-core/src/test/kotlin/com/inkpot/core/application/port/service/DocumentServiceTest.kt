package com.inkpot.core.application.port.service

import com.inkpot.core.application.InkpotCore
import com.inkpot.core.application.port.store.DocumentDto
import com.inkpot.core.application.port.store.DocumentStore
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class DocumentServiceTest {

    companion object {
        private const val AUTHOR = "author"
        private const val TITLE = "title"
        private const val CONTENT = "content"
    }

    private lateinit var documentStore: DocumentStore
    private lateinit var documentService: DocumentService

    @BeforeEach
    internal fun setUp() {
        documentStore = mock()
        documentService = InkpotCore.createContext(documentStore).documentService()
    }

    @Test
    internal fun createDocument() {
        val createDocument = CreateDocument(AUTHOR, TITLE, CONTENT)

        val document = documentService.createDocument(createDocument)

        var uuid: UUID
        argumentCaptor<DocumentDto>().apply {
            verify(documentStore).save(capture())

            uuid = firstValue.uuid
            assertNotNull(uuid)
            assertEquals(AUTHOR, firstValue.author)
            assertEquals(TITLE, firstValue.title)
            assertEquals(CONTENT, firstValue.content)
        }

        assertNotNull(document.uuid)
        assertEquals(uuid, document.uuid)
        assertEquals(AUTHOR, document.author)
        assertEquals(TITLE, document.title)
        assertEquals(CONTENT, document.content)
    }

    @Test
    internal fun `find an existing document`() {
        val uuid = UUID.randomUUID()
        whenever(documentStore.find(uuid)).thenReturn(Optional.of(DocumentDto(uuid, AUTHOR, TITLE, CONTENT)))

        val document = documentService.findDocument(uuid)

        argumentCaptor<UUID>().apply {
            verify(documentStore).find(capture())

            assertEquals(uuid, firstValue)
        }

        assertNotNull(document)
        assertEquals(uuid, document.get().uuid)
        assertEquals(AUTHOR, document.get().author)
        assertEquals(TITLE, document.get().title)
        assertEquals(CONTENT, document.get().content)
    }

    @Test
    internal fun `find an non existing document`() {
        val uuid = UUID.randomUUID()
        whenever(documentStore.find(uuid)).thenReturn(Optional.empty())

        val document = documentService.findDocument(uuid)

        argumentCaptor<UUID>().apply {
            verify(documentStore).find(capture())

            assertEquals(uuid, firstValue)
        }

        assertFalse(document.isPresent)
    }

    @Test
    internal fun `find all documents`() {
        val uuid0 = UUID.randomUUID()
        val uuid1 = UUID.randomUUID()
        whenever(documentStore.findAll())
                .thenReturn(
                        setOf(
                                DocumentDto(uuid0, "${AUTHOR}0", "${TITLE}0", "${CONTENT}0"),
                                DocumentDto(uuid1, "${AUTHOR}1", "${TITLE}1", "${CONTENT}1")
                        )
                )


        val documents = documentService.findAllDocuments()

        verify(documentStore).findAll()

        assertEquals(2, documents.size)

        val document0 = documents.elementAt(0)
        assertEquals(uuid0, document0.uuid)
        assertEquals("${AUTHOR}0", document0.author)
        assertEquals("${TITLE}0", document0.title)
        assertEquals("${CONTENT}0", document0.content)

        val document1 = documents.elementAt(1)
        assertEquals(uuid1, document1.uuid)
        assertEquals("${AUTHOR}1", document1.author)
        assertEquals("${TITLE}1", document1.title)
        assertEquals("${CONTENT}1", document1.content)
    }

    @Test
    internal fun `delete document`() {
        val uuid = UUID.randomUUID()

        documentService.deleteDocument(uuid)

        argumentCaptor<UUID>().apply {
            verify(documentStore).delete(capture())

            assertEquals(uuid, firstValue)
        }
    }

}