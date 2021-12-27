package com.inkpot.api;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.InkpotCore;
import com.inkpot.core.application.port.store.AuthorDao;
import com.inkpot.core.application.port.store.DocumentDao;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class InkpotCoreConfig {

    @Inject
    @Produces
    @ApplicationScoped
    public CoreContext coreContext(AuthorDao authorDao, DocumentDao documentDao) {
        return InkpotCore.createContext(authorDao, documentDao);
    }

}
