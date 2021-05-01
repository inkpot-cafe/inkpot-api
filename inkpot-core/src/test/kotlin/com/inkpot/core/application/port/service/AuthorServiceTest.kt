package com.inkpot.core.application.port.service

import com.inkpot.core.application.InkpotCore
import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorDao
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class AuthorServiceTest {

    companion object {
        private const val NAME = "name"
        private val DOCUMENT_IDS = emptySet<UUID>()
    }

    private lateinit var authorDao: AuthorDao
    private lateinit var authorService: AuthorService

    @BeforeEach
    internal fun setUp() {
        authorDao = mock()
        authorService = InkpotCore.createContext(authorDao, mock()).authorService()
    }

    @Test
    internal fun `create author`() {
        val data = AuthorCreateData(NAME)

        val author = authorService.createAuthor(data)

        val uuid: UUID
        argumentCaptor<AuthorDto>().apply {
            verify(authorDao).save(capture())

            uuid = firstValue.id
            assertNotNull(uuid)
            assertEquals(NAME, firstValue.name)
        }
        assertNotNull(author)
        assertEquals(uuid, author.id)
        assertEquals(NAME, author.name)
        assertEquals(DOCUMENT_IDS, author.documentIds)
    }

    @Test
    internal fun `find author`() {
        val uuid = UUID.randomUUID()
        whenever(authorDao.find(uuid)).thenReturn(Optional.of(anAuthorDto(uuid)))

        val author = authorService.findAuthor(uuid)

        verify(authorDao).find(uuid)

        assertTrue(author.isPresent)
        assertEquals(uuid, author.get().id)
        assertEquals(NAME, author.get().name)
        assertEquals(DOCUMENT_IDS, author.get().documentIds)
    }

    @Test
    internal fun `find author not found`() {
        val uuid = UUID.randomUUID()
        whenever(authorDao.find(uuid)).thenReturn(Optional.empty())

        val author = authorService.findAuthor(uuid)

        verify(authorDao).find(uuid)

        assertFalse(author.isPresent)
    }

    @Test
    internal fun `find all authors`() {
        val uuid = UUID.randomUUID()
        whenever(authorDao.findAll()).thenReturn(setOf(anAuthorDto(uuid)))

        val authors = authorService.findAllAuthors()

        verify(authorDao).findAll()

        assertEquals(1, authors.size)
        assertEquals(uuid, authors.elementAt(0).id)
        assertEquals(NAME, authors.elementAt(0).name)
        assertEquals(DOCUMENT_IDS, authors.elementAt(0).documentIds)
    }

    private fun anAuthorDto(uuid: UUID) = AuthorDto(uuid, NAME, DOCUMENT_IDS)

    @Test
    internal fun `delete author`() {
        val uuid = UUID.randomUUID()

        authorService.deleteAuthor(uuid)

        verify(authorDao).delete(uuid)
    }

}