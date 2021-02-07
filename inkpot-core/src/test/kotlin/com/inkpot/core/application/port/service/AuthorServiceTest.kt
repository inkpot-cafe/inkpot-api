package com.inkpot.core.application.port.service

import com.inkpot.core.application.InkpotCore
import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.application.port.store.DocumentDto
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class AuthorServiceTest {

    companion object {
        private const val NAME = "name"
    }

    private lateinit var authorStore: AuthorStore
    private lateinit var authorService: AuthorService

    @BeforeEach
    internal fun setUp() {
        authorStore = mock()
        authorService = InkpotCore.createContext(authorStore, mock()).authorService()
    }

    @Test
    internal fun `create author`() {
        val data = AuthorCreateData(NAME)

        val author = authorService.createAuthor(data)

        val uuid: UUID
        argumentCaptor<AuthorDto>().apply {
            verify(authorStore).save(capture())

            uuid = firstValue.uuid
            assertNotNull(uuid)
            assertEquals(NAME, firstValue.name)
        }
        assertNotNull(author)
        assertEquals(author.name, NAME)
        assertEquals(author.uuid, uuid)
    }

    @Test
    internal fun `find author`() {
        val uuid = UUID.randomUUID()
        whenever(authorStore.find(uuid)).thenReturn(Optional.of(AuthorDto(uuid, NAME)))

        val author = authorService.findAuthor(uuid)

        verify(authorStore).find(uuid)

        assertTrue(author.isPresent)
        assertEquals(author.get().uuid, uuid)
        assertEquals(author.get().name, NAME)
    }
}