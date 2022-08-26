/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.util;

import org.hyperledger.aries.config.UriUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UriUtilTest {

    @Test
    void testHttpToWs() {
        Assertions.assertEquals("ws://localhost/ws", UriUtil.httpToWs("http://localhost"));
        Assertions.assertEquals("wss://localhost/ws", UriUtil.httpToWs("https://localhost"));
        Assertions.assertEquals("ws://localhost:8080/ws", UriUtil.httpToWs("http://localhost:8080"));
        Assertions.assertEquals("wss://localhost:8080/ws", UriUtil.httpToWs("https://localhost:8080"));
    }
}
