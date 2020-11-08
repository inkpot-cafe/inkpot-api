package com.inkpot.core.application

import com.inkpot.core.application.port.store.DocumentStore

object InkpotCore {

    @JvmStatic
    fun createContext(documentStore: DocumentStore): CoreContext = InternalCoreContext(documentStore)

}