package com.inkpot.core.domain

import com.inkpot.core.AUTHOR
import com.inkpot.core.CONTENT
import com.inkpot.core.RANDOM_UUID
import com.inkpot.core.TITLE
import com.inkpot.core.domain.hook.Hook
import com.inkpot.core.domain.hook.HookHolder
import com.inkpot.core.store.DocumentDto
import com.inkpot.core.store.DocumentStore
import com.nhaarman.mockitokotlin2.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class DocumentRepositoryTest {
    lateinit var store: DocumentStore;
    lateinit var hookHolder: HookHolder;
    lateinit var repository: DocumentRepository

    @BeforeEach
    internal fun setUp() {
        store = mock()
        hookHolder = HookHolder()
        repository = InternalDocumentRepository(hookHolder, store)
    }

    @Test
    internal fun save() {
        val document = InternalDocumentFactory()
            .uuid(RANDOM_UUID)
            .title(TITLE)
            .author(AUTHOR)
            .content(CONTENT)
            .create()
        repository.save(document)

        argumentCaptor<DocumentDto>().apply {
            verify(store).save(capture())
            val documentDto = firstValue

            assertEquals(RANDOM_UUID, documentDto.uuid)
            assertEquals(TITLE, documentDto.title)
            assertEquals(AUTHOR, documentDto.author)
            assertEquals(CONTENT, documentDto.content)
        }
    }

    @Test
    internal fun `execute before hook on save`() {
        val document: Document = InternalDocumentFactory()
            .uuid(RANDOM_UUID)
            .title(TITLE)
            .author(AUTHOR)
            .content(CONTENT)
            .create()

        val hook: Hook = mock()
        hookHolder.addBefore("save", hook)

        repository.save(document)

        inOrder(hook, store).apply {
            verify(hook).execute()
            verify(store).save(any())
        }
    }

    @Test
    internal fun `execute after hook on save`() {
        val document: Document = InternalDocumentFactory()
            .uuid(RANDOM_UUID)
            .title(TITLE)
            .author(AUTHOR)
            .content(CONTENT)
            .create()

        val hook: Hook = mock()
        hookHolder.addAfter("save", hook)

        repository.save(document)

        inOrder(store, hook).apply {
            verify(store).save(any())
            verify(hook).execute()
        }
    }


    @Test
    internal fun find() {
        whenever(store.find(RANDOM_UUID)).thenReturn(
            DocumentDto(
                RANDOM_UUID,
                AUTHOR,
                TITLE,
                CONTENT
            )
        )

        val document = repository.find(RANDOM_UUID)

        verify(store).find(eq(RANDOM_UUID))

        assertEquals(RANDOM_UUID, document?.getUuid())
        assertEquals(TITLE, document?.getTitle())
        assertEquals(AUTHOR, document?.getAuthor())
        assertEquals(CONTENT, document?.getContent())
    }

    @Test
    internal fun `find null`() {
        val document = repository.find(RANDOM_UUID)

        verify(store).find(eq(RANDOM_UUID))

        assertNull(document)
    }

    @Test
    internal fun `execute hook before find`() {
        val hook: Hook = mock()
        hookHolder.addBefore("find", hook)

        repository.find(RANDOM_UUID)

        inOrder(store, hook) {
            verify(hook).execute()
            verify(store).find(eq(RANDOM_UUID))
        }
    }

    @Test
    internal fun `execute hook after find`() {
        val hook: Hook = mock()
        hookHolder.addAfter("find", hook)

        repository.find(RANDOM_UUID)

        inOrder(store, hook) {
            verify(store).find(eq(RANDOM_UUID))
            verify(hook).execute()
        }
    }

    @Test
    internal fun findAll() {
        val id0 = UUID.randomUUID()
        val documentDto1 = DocumentDto(
            id0,
            AUTHOR,
            TITLE,
            CONTENT
        )
        val id1 = UUID.randomUUID()
        val documentDto2 = DocumentDto(
            id1,
            AUTHOR,
            TITLE,
            CONTENT
        )

        whenever(store.findAll()).thenReturn(setOf(documentDto1, documentDto2))

        val documents = repository.findAll()

        verify(store).findAll()

        assertEquals(2, documents.size)
        assertEquals(id0, documents.elementAt(0).getUuid())
        assertEquals(id1, documents.elementAt(1).getUuid())
    }

    @Test
    internal fun `execute hook before findAll`() {
        val hook: Hook = mock()
        hookHolder.addBefore("findAll", hook)

        repository.findAll()

        inOrder(store, hook) {
            verify(hook).execute()
            verify(store).findAll()
        }
    }

    @Test
    internal fun `execute hook after findAll`() {
        val hook: Hook = mock()
        hookHolder.addAfter("findAll", hook)

        repository.findAll()

        inOrder(store, hook) {
            verify(store).findAll()
            verify(hook).execute()
        }
    }

    @Test
    internal fun delete() {
        repository.delete(RANDOM_UUID)

        verify(store).delete(eq(RANDOM_UUID))
    }

    @Test
    internal fun `execute hook before delete`() {
        val hook: Hook = mock()
        hookHolder.addBefore("delete", hook)

        repository.delete(RANDOM_UUID)

        inOrder(store, hook) {
            verify(hook).execute()
            verify(store).delete(RANDOM_UUID)
        }
    }

    @Test
    internal fun `execute hook after delete`() {
        val hook: Hook = mock()
        hookHolder.addAfter("delete", hook)

        repository.delete(RANDOM_UUID)

        inOrder(store, hook) {
            verify(store).delete(RANDOM_UUID)
            verify(hook).execute()
        }
    }

}