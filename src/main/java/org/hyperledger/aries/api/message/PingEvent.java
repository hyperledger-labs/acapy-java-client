/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Ping response Webhook event
 */
@Data @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true) @Builder
public final class PingEvent {

    private String comment;

    private String connectionId;

    private Boolean responded;

    private String threadId;

    private String state;

    public static PingEvent of(String threadId, String state) {
        return PingEvent.builder().threadId(threadId).state(state).build();
    }

    public static PingEvent of(String threadId) {
        return PingEvent.builder().threadId(threadId).build();
    }

    public boolean hasResponded() {
        return responded != null && responded;
    }

    public boolean stateIsReceived() {
        return "received".equals(state);
    }
}
