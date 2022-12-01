/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.did_exchange;

import okhttp3.mockwebserver.MockResponse;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class MockedDidExchangeTest extends MockedTestBase {

    @Test
    void testCreateRequest() throws Exception {
        String json = FileLoader.load("files/didexchange/didexchange-create-request.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<ConnectionRecord> c = ac.didExchangeCreateRequest(DidExchangeCreateRequestFilter
                .builder()
                .theirPublicDid("F6dB7dMVHUQSC64qemnBi7")
                .build());
        Assertions.assertTrue(c.isPresent());
        Assertions.assertEquals("request-sent", c.get().getRfc23State());
        Assertions.assertEquals(ConnectionRecord.ConnectionProtocol.DID_EXCHANGE_V1, c.get().getConnectionProtocol());
    }
}
