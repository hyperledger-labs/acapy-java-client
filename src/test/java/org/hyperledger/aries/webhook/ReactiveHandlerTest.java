/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.connection.ConnectionState;
import org.hyperledger.aries.webhook.reactive.ReactiveEventHandler;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;

public class ReactiveHandlerTest {

    @Test
    void testReactiveSink() {
        ReactiveEventHandler h = new ReactiveEventHandler();

        ConnectionRecord c1 = new ConnectionRecord();
        c1.setState(ConnectionState.REQUEST);
        ConnectionRecord c2 = new ConnectionRecord();
        c2.setState(ConnectionState.ACTIVE);

        h.connections().filter(ConnectionRecord::stateIsActive).subscribe(System.out::println);
        h.connections().filter(ConnectionRecord::stateIsActive).subscribe(System.out::println);

        //h.handleConnection(c1);
        //h.handleConnection(c2);

        //h.handleConnection(c1);
        //h.handleConnection(c2);

        //subscribe.dispose();
    }
}
