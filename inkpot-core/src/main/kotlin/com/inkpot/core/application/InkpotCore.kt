package com.inkpot.core.application

import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.application.port.store.DocumentStore

object InkpotCore {

    @JvmStatic
    fun createContext(authorStore: AuthorStore, documentStore: DocumentStore): CoreContext =
        InternalCoreContext(authorStore, documentStore)

}