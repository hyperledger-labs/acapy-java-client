/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import okhttp3.mockwebserver.MockResponse;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

class MockedConnectionTest extends MockedTestBase {

    @Test
    void testGetConnections() throws Exception {
        String json = FileLoader.load("files/connections.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<List<ConnectionRecord>> c = ac.connections();
        Assertions.assertTrue(c.isPresent());
        Assertions.assertEquals(13, c.get().size());
    }

    @Test
    void testGetConnectionsFiltered() throws Exception {
        String json = FileLoader.load("files/connectionsActive.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<List<ConnectionRecord>> c = ac
                .connections(ConnectionFilter.builder().state(ConnectionState.ACTIVE).build());
        Assertions.assertTrue(c.isPresent());
        Assertions.assertEquals(9, c.get().size());
    }

    @Test
    void testGetConnectionIds() throws Exception {
        String json = FileLoader.load("files/connections.json");
        server.enqueue(new MockResponse().setBody(json));

        List<String> c = ac.connectionIds();
        Assertions.assertEquals(13, c.size());
    }

    @Test
    void testGetActiveConnectionIds() throws Exception {
        String json = FileLoader.load("files/connectionsActive.json");
        server.enqueue(new MockResponse().setBody(json));

        List<String> c = ac.connectionIds(ConnectionFilter.builder().state(ConnectionState.ACTIVE).build());
        Assertions.assertEquals(9, c.size());
    }

    @Test
    void testCreateInvitation() throws Exception {
        String json = FileLoader.load("files/invitation.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<CreateInvitationResponse> inv = ac
                .connectionsCreateInvitation(CreateInvitationRequest.builder().build());
        Assertions.assertTrue(inv.isPresent());
        Assertions.assertTrue(inv.get().getConnectionId().startsWith("d16dc0bf"));
    }

    @Test
    void testReceiveInvitation() throws Exception {
        String json = FileLoader.load("files/connection.json");
        server.enqueue(new MockResponse().setBody(json));

        final Optional<ConnectionRecord> con = ac.connectionsReceiveInvitation(new ReceiveInvitationRequest(), null);
        Assertions.assertTrue(con.isPresent());
        Assertions.assertTrue(con.get().getConnectionId().startsWith("bc349b33"));
        Assertions.assertEquals(ConnectionRecord.ConnectionProtocol.CONNECTION_V1, con.get().getConnectionProtocol());
    }

    @Test
    void testConnectionProtocol() {
        ConnectionRecord con = new ConnectionRecord();
        con.setConnectionProtocol(ConnectionRecord.ConnectionProtocol.DID_EXCHANGE_V1);
        String json = gson.toJson(con);
        ConnectionRecord deserialized = gson.fromJson(json, ConnectionRecord.class);
        Assertions.assertEquals(con, deserialized);
    }

}
