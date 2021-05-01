package com.inkpot.core.application.port.service

import com.inkpot.core.application.InkpotCore
import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorDao
import com.inkpot.core.application.port.store.DocumentDto
import com.inkpot.core.application.port.store.DocumentDao
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
        private const val TITLE = "title"
        private const val CONTENT = "content"
        private val DOCUMENT_IDS = emptySet<UUID>()
    }

    private lateinit var authorDao: AuthorDao
    private lateinit var documentDao: DocumentDao
    private lateinit var documentService: DocumentService

    @BeforeEach
    internal fun setUp() {
        documentDao = mock()
        authorDao = mock()
        documentService = InkpotCore.createContext(authorDao, documentDao).documentService()
    }

    @Test
    internal fun createDocument() {
        val authorId = UUID.randomUUID()
        val createDocument = DocumentCreateData(authorId, TITLE, CONTENT)
        whenever(authorDao.find(authorId)).thenReturn(Optional.of(aAuthorDto(authorId)))

        val document = documentService.createDocument(createDocument)

        var uuid: UUID
        argumentCaptor<DocumentDto>().apply {
            verify(documentDao).save(capture())

            uuid = firstValue.id
            assertNotNull(uuid)
            assertEquals(authorId, firstValue.authorId)
            assertEquals(TITLE, firstValue.title)
            assertEquals(CONTENT, firstValue.content)
        }

        assertNotNull(document.id)
        assertEquals(uuid, document.id)
        assertEquals(authorId, document.authorId)
        assertEquals(TITLE, document.title)
        assertEquals(CONTENT, document.content)
    }

    @Test
    internal fun `find an existing document`() {
        val uuid = UUID.randomUUID()
        val authorId = UUID.randomUUID()
        whenever(documentDao.find(uuid)).thenReturn(Optional.of(aDocumentDto(uuid, authorId)))

        val document = documentService.findDocument(uuid)

        argumentCaptor<UUID>().apply {
            verify(documentDao).find(capture())

            assertEquals(uuid, firstValue)
        }

        assertNotNull(document)
        assertEquals(uuid, document.get().id)
        assertEquals(authorId, document.get().authorId)
        assertEquals(TITLE, document.get().title)
        assertEquals(CONTENT, document.get().content)
    }

    @Test
    internal fun `find an non existing document`() {
        val uuid = UUID.randomUUID()
        whenever(documentDao.find(uuid)).thenReturn(Optional.empty())

        val document = documentService.findDocument(uuid)

        argumentCaptor<UUID>().apply {
            verify(documentDao).find(capture())

            assertEquals(uuid, firstValue)
        }

        assertFalse(document.isPresent)
    }

    @Test
    internal fun `find all documents`() {
        val uuid = UUID.randomUUID()
        val authorId = UUID.randomUUID()
        whenever(documentDao.findAll())
            .thenReturn(setOf(aDocumentDto(uuid, authorId)))

        val documents = documentService.findAllDocuments()

        verify(documentDao).findAll()

        assertEquals(1, documents.size)

        val document = documents.elementAt(0)
        assertEquals(uuid, document.id)
        assertEquals(authorId, document.authorId)
        assertEquals(TITLE, document.title)
        assertEquals(CONTENT, document.content)
    }

    @Test
    internal fun `delete document`() {
        val uuid = UUID.randomUUID()

        documentService.deleteDocument(uuid)

        argumentCaptor<UUID>().apply {
            verify(documentDao).delete(capture())

            assertEquals(uuid, firstValue)
        }
    }

    private fun aDocumentDto(uuid: UUID, authorId: UUID) = DocumentDto(uuid, authorId, TITLE, CONTENT)

    private fun aAuthorDto(authorId: UUID) = AuthorDto(authorId, "name", DOCUMENT_IDS)
}
