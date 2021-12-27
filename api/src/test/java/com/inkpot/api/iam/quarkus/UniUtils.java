package com.inkpot.api.iam.quarkus;

import io.smallrye.mutiny.Uni;

public class UniUtils {
    public static <I> I readItem(Uni<I> uni) {
        return uni.subscribe().asCompletionStage().join();
    }
}
