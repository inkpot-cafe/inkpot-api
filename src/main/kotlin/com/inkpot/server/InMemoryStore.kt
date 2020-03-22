package com.inkpot.server

import com.inkpot.core.Document
import com.inkpot.core.Store
import java.util.*
import kotlin.collections.HashMap

object InMemoryStore : Store {

    private val documents = HashMap<UUID, Document>()

    override fun save(document: Document) {
        documents[document.uuid] = document
    }

    override fun load(uuid: UUID): Document? {
        return documents[uuid]
    }

    override fun delete(uuid: UUID) {
        documents.remove(uuid)
    }

}