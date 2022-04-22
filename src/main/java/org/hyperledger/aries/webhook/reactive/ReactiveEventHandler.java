/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook.reactive;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.webhook.EventParser;
import org.hyperledger.aries.webhook.EventType;
import org.hyperledger.aries.webhook.IEventHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class ReactiveEventHandler implements IEventHandler {

    private final EventParser parser = new EventParser();

    private final Sinks.Many<ConnectionRecord> connectionsSink = Sinks.many().multicast().onBackpressureBuffer(100, false);
    // replay().limit(100)
    // multicast().onBackpressureBuffer(100, false);

    public void handleEvent(String topic, String payload) {
        handleEvent(null, topic, payload);
    }

    public void handleEvent(String walletId, String topic, String payload) {
        log.debug("walletId: {}, topic: {}, payload: {}", walletId, topic, payload);

        // TODO add handleForWalletId constructor arg, this allows filtering if needed.
        // TODO ws client needs handler list for that

        try {
            if (EventType.CONNECTIONS.valueEquals(topic)) {
                parser.parseValueSave(payload, ConnectionRecord.class, connectionsSink::tryEmitNext);
            }
        } catch (Throwable e) {
            log.error("Error in reactive event handler:", e);
        }
    }

    public Flux<ConnectionRecord> connections() {
        return connectionsSink.asFlux();
    }
}
