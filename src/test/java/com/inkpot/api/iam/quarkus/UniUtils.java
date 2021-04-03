package com.inkpot.api.iam.quarkus;

import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.mutiny.Uni;

public class UniUtils {
    public static SecurityIdentity readSecurityIdentity(Uni<SecurityIdentity> uni) {
        return uni.subscribe().asCompletionStage().join();
    }
}
