package com.inkpot.api;

import com.inkpot.core.CoreContext;
import com.inkpot.core.InkpotCore;
import com.inkpot.core.store.DocumentStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InkpotApi {

    private final DocumentStore store;

    @Autowired
    public InkpotApi(DocumentStore store) {
        this.store = store;
    }

    @Bean
    public CoreContext coreContext() {
        return InkpotCore.createContext(store);
    }

    public static void main(String[] args) {
        SpringApplication.run(InkpotApi.class, args);
    }

}
