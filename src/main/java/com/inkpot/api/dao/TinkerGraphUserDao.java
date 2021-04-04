package com.inkpot.api.dao;

import com.inkpot.api.iam.User;
import com.inkpot.api.iam.UserDao;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.eclipse.microprofile.config.ConfigProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static com.inkpot.api.iam.EncryptionUtil.sha512;

@ApplicationScoped
public class TinkerGraphUserDao implements UserDao {

    public static final String USER_LABEL = "user";
    public static final String PASSWORD_KEY = "password";

    private final GraphTraversalSource g;

    @Inject
    public TinkerGraphUserDao(GraphTraversalSource g) {
        this.g = g;
    }

    @PostConstruct
    public void defaultAdmin() {
        var username = ConfigProvider.getConfig().getValue("inkpot.admin.username", String.class);
        var password = ConfigProvider.getConfig().getValue("inkpot.admin.password", String.class);

        g.addV(USER_LABEL)
                .property(T.id, username)
                .property(PASSWORD_KEY, sha512(password))
                .iterate();
    }

    @Override
    public Optional<User> readUser(String username) {
        var vertex = g.V().hasLabel(USER_LABEL)
                .hasId(username)
                .tryNext();
        if (vertex.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(User.builder()
                .username(vertex.get().id().toString())
                .encryptedPassword(vertex.get().property(PASSWORD_KEY).value().toString())
                .build());
    }
}
