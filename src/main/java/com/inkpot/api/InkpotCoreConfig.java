package com.inkpot.api;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.InkpotCore;
import com.inkpot.core.application.port.store.DocumentStore;

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
