package com.inkpot.server;

import com.inkpot.core.CoreContext;
import com.inkpot.core.InkpotCore;
import com.inkpot.core.domain.Document;
import com.inkpot.core.domain.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;

@SpringBootApplication
public class Application {

    @Autowired
    private Store store;

    @Bean
    public CoreContext getContext() {
        return InkpotCore.createContext(store);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Component
    static class AppStore implements Store {

        Map<UUID, Document> documents = new HashMap<>();

        @Override
        public void save(@NonNull Document document) {
            documents.put(document.getUuid(), document);
        }

        @Override
        public Document load(@NonNull UUID uuid) {
            return documents.get(uuid);
        }

        @Override
        public void delete(@NonNull UUID uuid) {
            documents.remove(uuid);
        }

        @NonNull
        @Override
        public Set<Document> readAll() {
            return Set.copyOf(documents.values());
        }
    }

}
