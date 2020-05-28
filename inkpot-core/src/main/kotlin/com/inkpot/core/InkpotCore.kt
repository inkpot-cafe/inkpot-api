package com.inkpot.core

import com.inkpot.core.store.DocumentStore

object InkpotCore {

    @JvmStatic
    fun createContext(documentStore: DocumentStore): CoreContext = InternalCoreContext(documentStore)

}