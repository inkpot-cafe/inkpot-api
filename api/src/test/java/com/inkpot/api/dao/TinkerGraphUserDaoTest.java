package com.inkpot.api.dao;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static com.inkpot.api.iam.EncryptionUtil.sha256;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class TinkerGraphUserDaoTest {

    private static final String NON_PRESENT_USERNAME = "nonPresentUsername";
    @Inject
    TinkerGraphUserDao tinkerGraphUserDao;

    @InjectSpy
    GraphTraversalSource g;

    @ConfigProperty(name = "inkpot.admin.username")
    String username;
    @ConfigProperty(name = "inkpot.admin.password")
    String password;


    @Test
    void defaultUser() {
        // an interaction with the instance is necessary to call @PostConstruct in unit tests
        // it's a kind of quarkus bug :/
        tinkerGraphUserDao.toString();

        var vertex = g.V().hasLabel(TinkerGraphUserDao.USER_LABEL).
                hasId(username)
                .tryNext();

        assertThat(vertex.isPresent()).isTrue();
        assertThat(vertex.get().property(TinkerGraphUserDao.PASSWORD_KEY).value().toString()).isEqualTo(sha256(password));
    }

    @Test
    void readUser() {
        var user = tinkerGraphUserDao.readUser(username);

        assertThat(user.isPresent()).isTrue();
        assertThat(user.get().getUsername()).isEqualTo(username);
        assertThat(user.get().getEncryptedPassword()).isEqualTo(sha256(password));
    }

    @Test
    void readUserNonPresent() {
        var user = tinkerGraphUserDao.readUser(NON_PRESENT_USERNAME);

        assertThat(user.isEmpty()).isTrue();
    }
}
