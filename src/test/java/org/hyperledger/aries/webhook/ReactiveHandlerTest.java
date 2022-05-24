/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.Disposable;

public class ReactiveHandlerTest {

    @Test
    void testSubscribeAfterDispose() {
        ReactiveEventHandler h = new ReactiveEventHandler();
        Disposable s1 = h.connection().subscribe(System.out::println);
        Disposable s2 = h.connection().subscribe(System.out::println);

        h.handleEvent("connections", "{}");

        s1.dispose();
        s2.dispose();

        Disposable s3 = h.connection().subscribe(System.out::println);

        h.handleEvent("connections", "{}");

        s3.dispose();
    }

    @Test
    void testBlockingGet() {
        ReactiveEventHandler h1 = new ReactiveEventHandler();

        h1.handleEvent("1", "connections", "{\"state\": \"active\"}");


        ConnectionRecord c1 = h1.connection().filter(ConnectionRecord::stateIsActive).blockFirst();
        Assertions.assertNotNull(c1);
        Assertions.assertTrue(c1.stateIsActive());
    }
}
