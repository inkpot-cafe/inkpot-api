package com.inkpot.core.application

import com.inkpot.core.application.port.store.AuthorDao
import com.inkpot.core.application.port.store.DocumentDao

object InkpotCore {

    @JvmStatic
    fun createContext(authorDao: AuthorDao, documentDao: DocumentDao): CoreContext =
        InternalCoreContext(authorDao, documentDao)

}