/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.trustping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Ping response webhook event
 */
@Data @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true) @Builder
public final class PingEvent {

    private String comment;

    private String connectionId;

    private String threadId;

    private PingEventState state;

    /** only set when being pinged */
    private Boolean responded;

    public static PingEvent of(String threadId, PingEventState state) {
        return PingEvent.builder().threadId(threadId).state(state).build();
    }

    public static PingEvent of(String threadId) {
        return PingEvent.builder().threadId(threadId).build();
    }

    public boolean stateIsResponseReceived() {
        return PingEventState.RESPONSE_RECEIVED.equals(state);
    }
}
