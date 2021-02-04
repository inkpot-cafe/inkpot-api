package com.inkpot.api;

import com.inkpot.core.CoreContext;
import com.inkpot.core.InkpotCore;
import com.inkpot.core.store.DocumentStore;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@ApplicationScoped
public class InkpotCoreConfig {

    @Inject
    @Produces
    public CoreContext coreContext(DocumentStore store) {
        return InkpotCore.createContext(store);
    }

}
