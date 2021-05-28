/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.IntegrationTestBase;
import org.hyperledger.aries.api.exception.AriesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ConnectionRecordTest extends IntegrationTestBase {

    @Test
    void testDeleteConnection() {
        assertThrows(AriesException.class, () -> ac.connectionsRemove("1"));
    }

    @Test
    void testGetConnections() throws Exception {
        final Optional<List<ConnectionRecord>> connections = ac.connections(
                ConnectionFilter.builder().state(ConnectionState.ACTIVE).build());
        assertTrue(connections.isPresent());
        assertEquals(0, connections.get().size());
    }

    @Test
    void testGetConnectionsWrongFilter() {
        assertThrows(AriesException.class, () -> ac.connections(
            ConnectionFilter.builder().myDid("wrong_format").build()));
    }

    @Test
    void testCreateInvitation() throws Exception {
        final Optional<CreateInvitationResponse> inv = ac.connectionsCreateInvitation(
                CreateInvitationRequest
                        .builder()
                        .recipientKeys(List.of())
                        .build(),
                CreateInvitationParams
                        .builder()
                        .autoAccept(Boolean.TRUE)
                        .build());
        assertTrue(inv.isPresent());
        assertNotNull(inv.get().getInvitationUrl());
        log.debug("{}", inv.get());
    }

    @Test
    void testMetadata() throws Exception {
        ac.connectionsCreateInvitation(
                CreateInvitationRequest
                        .builder()
                        .recipientKeys(List.of())
                        .build(),
                CreateInvitationParams
                        .builder()
                        .autoAccept(Boolean.TRUE)
                        .build());

        Optional<List<ConnectionRecord>> connections = ac.connections();
        Assertions.assertTrue(connections.isPresent());
        Assertions.assertEquals(1, connections.get().size());

        String connectionId = connections.get().get(0).getConnectionId();
        Optional<Map<String, String>> metadata = ac.connectionsSetMetadata(connectionId, ConnectionSetMetaDataRequest
                .builder()
                .metadata(Map.of("key1", "value1", "key2", "value2"))
                .build());
        Assertions.assertTrue(metadata.isPresent());
        Assertions.assertEquals("value2", metadata.get().get("key2"));

        metadata = ac.connectionsGetMetadata(connectionId);
        Assertions.assertTrue(metadata.isPresent());
        Assertions.assertEquals("value2", metadata.get().get("key2"));

        Optional<String> singleValue = ac.connectionsGetMetadata(connectionId, "key1");
        Assertions.assertTrue(singleValue.isPresent());
        Assertions.assertEquals("value1", singleValue.get());
    }
}
