package com.inkpot.server;

import com.inkpot.core.Context;
import com.inkpot.core.Document;
import com.inkpot.core.Inkpot;
import com.inkpot.core.Store;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class Application {

    @Bean
    public Context getContext() {
        return Inkpot.createContext(new AppStore());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

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
    }

}
