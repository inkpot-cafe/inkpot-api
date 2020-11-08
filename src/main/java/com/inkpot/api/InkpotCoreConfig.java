package com.inkpot.api;

import com.inkpot.core.application.CoreContext;
import com.inkpot.core.application.InkpotCore;
import com.inkpot.core.application.port.store.DocumentStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InkpotCoreConfig {

    @Bean
    @Autowired
    public CoreContext coreContext(DocumentStore store) {
        return InkpotCore.createContext(store);
    }

}
