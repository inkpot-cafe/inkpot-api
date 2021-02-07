package com.inkpot.core.application.port.service

import com.inkpot.core.application.InkpotCore
import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.application.port.store.DocumentDto
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class AuthorServiceTest {

    private lateinit var authorStore: AuthorStore
    private lateinit var authorService: AuthorService

    @BeforeEach
    internal fun setUp() {
        authorStore = mock()
        authorService = InkpotCore.createContext(authorStore, mock()).authorService()
    }

    @Test
    internal fun `create author`() {
        val data = AuthorCreateData("name")

        val author = authorService.createAuthor(data)

        val uuid: UUID
        argumentCaptor<AuthorDto>().apply {
            verify(authorStore).save(capture())

            uuid = firstValue.uuid
            assertNotNull(uuid)
            assertEquals("name", firstValue.name)
        }
        assertNotNull(author)
        assertEquals(author.name, "name")
        assertEquals(author.uuid, uuid)
    }

}