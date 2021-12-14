/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.MultiIntegrationTestBase;
import org.hyperledger.aries.api.exception.AriesException;
import org.hyperledger.acy_py.generated.model.ConnectionInvitation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class MultiConnectionRecordTest extends MultiIntegrationTestBase {

    protected String extraAgentArgs(int agentNum) {
        switch (agentNum) {
            case 1:
                return " --genesis-url https://indy-test.bosch-digital.de/genesis"
                     + " --auto-ping-connection"
                     + " --wallet-type indy"
                     + " --wallet-name agent1"
                     + " --wallet-key agent1key"
                     + " --auto-provision";
            case 2:
                return " --genesis-url https://indy-test.bosch-digital.de/genesis"
                     + " --auto-ping-connection"
                     + " --wallet-type indy"
                     + " --wallet-name agent2"
                     + " --wallet-key agent2key"
                     + " --auto-provision";
            default:
                return super.extraAgentArgs(agentNum);
        }
    }

    @Test
    void testCreateConnectionBetweenAgents() throws Exception {
        String alias_1 = "my_test_1";
        String alias_2 = "my_test_2";
        final Optional<CreateInvitationResponse> inv = ac.connectionsCreateInvitation(
                CreateInvitationRequest
                        .builder()
                        .recipientKeys(List.of())
                        .build(),
                CreateInvitationParams
                        .builder()
                        .autoAccept(Boolean.TRUE)
                        .alias(alias_1)
                        .build());
        assertTrue(inv.isPresent());
        assertNotNull(inv.get().getInvitationUrl());

        ConnectionInvitation conn_inv = inv.get().getInvitation();
        final Optional<ConnectionRecord> conn2 = ac2.connectionsReceiveInvitation(
                ReceiveInvitationRequest
                        .builder()
                        .id(conn_inv.getAtId())
                        .type(conn_inv.getAtType())
                        .did(conn_inv.getDid())
                        .imageUrl(conn_inv.getImageUrl())
                        .label(conn_inv.getLabel())
                        .recipientKeys(conn_inv.getRecipientKeys())
                        .routingKeys(conn_inv.getRoutingKeys())
                        .serviceEndpoint(conn_inv.getServiceEndpoint())
                        .build(),
                ConnectionReceiveInvitationFilter
                        .builder()
                        .alias(alias_2)
                        .autoAccept(false)
                        .build());
        assertTrue(conn2.isPresent());

        final Optional<ConnectionRecord> conn2_updated = ac2.connectionsAcceptInvitation(
                conn2.get().getConnectionId(),
                null);
        assertTrue(conn2_updated.isPresent());

        // pause to allow the agent chatter to complete
        Thread.sleep(4000);
        
        // now fetch connections and confirm everything completed successfully
        final Optional<List<ConnectionRecord>> connections = ac.connections(
                ConnectionFilter.builder().alias(alias_1).build());
        assertTrue(connections.isPresent());
        assertEquals(1, connections.get().size());
        assertEquals(alias_1, connections.get().get(0).getAlias());
        assertEquals(ConnectionState.ACTIVE, connections.get().get(0).getState());
        
        final Optional<List<ConnectionRecord>> connections2 = ac2.connections(
                ConnectionFilter.builder().alias(alias_2).build());
        assertTrue(connections2.isPresent());
        assertEquals(1, connections2.get().size());
        assertEquals(alias_2, connections2.get().get(0).getAlias());
        assertEquals(ConnectionState.ACTIVE, connections2.get().get(0).getState());
    }
}
