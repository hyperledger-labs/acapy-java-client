/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Web hook event
 *
 */
@Data @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
public final class PingEvent {

    private String threadId;

    private String state;

    public static PingEvent of(String threadId, String state) {
        return new PingEvent(threadId, state);
    }

    public static PingEvent of(String threadId) {
        return new PingEvent().setThreadId(threadId);
    }
}
