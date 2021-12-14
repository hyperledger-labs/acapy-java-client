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
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.acy_py.generated.model.DIDCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.hyperledger.aries.LedgerClient;
import org.hyperledger.aries.util.ledger.LedgerDIDCreate;
import org.hyperledger.aries.util.ledger.LedgerDIDResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EndorserConnectionRecordTest extends MultiIntegrationTestBase {

    public static final String INDY_LEDGER_URL = "http://test.bcovrin.vonx.io";
    public static final String ENDORSER_CONNECTION_ALIAS = "endorser";

    protected String getLedgerUrl() {
        return INDY_LEDGER_URL;
    }

    protected String extraAgentArgs(int agentNum) {
        switch (agentNum) {
            case 1:
                // agent 1 is the Author
                return " --genesis-url " + INDY_LEDGER_URL + "/genesis"
                     + " --auto-ping-connection"
                     + " --wallet-type indy"
                     + " --wallet-name agent1"
                     + " --wallet-key agent1key"
                     + " --auto-provision"
                     + " --endorser-protocol-role author"
                     + " --endorser-alias " + ENDORSER_CONNECTION_ALIAS
                     + " --auto-request-endorsement"
                     + " --auto-write-transactions";
            case 2:
                // agent 2 is the Endorser
                return " --genesis-url " + INDY_LEDGER_URL + "/genesis"
                     + " --auto-ping-connection"
                     + " --wallet-type indy"
                     + " --wallet-name agent2"
                     + " --wallet-key agent2key"
                     + " --auto-provision"
                     + " --endorser-protocol-role endorser"
                     + " --auto-endorse-transactions";
            default:
                return super.extraAgentArgs(agentNum);
        }
    }

    @Test
    void testCreateConnectionBetweenAuthorAndEndorser() throws Exception {
        String alias_1 = "my_agent_1";
        String alias_2 = "my_agent_2";

        // create a DID for each agent and make public
        lc = LedgerClient.builder()
            .url(INDY_LEDGER_URL)
            .build();

        DID localDid1 = ac.walletDidCreate(DIDCreate
                .builder()
                .build())
                .orElseThrow();
        assertNotNull(localDid1.getVerkey());
        Optional<LedgerDIDResponse> agent1Did = lc.ledgerDidCreate(
                LedgerDIDCreate
                    .builder()
                    .alias(alias_1)
                    .did(localDid1.getDid())
                    .verkey(localDid1.getVerkey())
                    .build());
        assertTrue(agent1Did.isPresent());
        assertNotNull(agent1Did.get().getDid());
        Optional<DID> localPublicDid1 = ac.walletDidPublic(localDid1.getDid());
        assertTrue(localPublicDid1.isPresent());
        assertNotNull(localPublicDid1.get().getVerkey());

        DID localDid2 = ac2.walletDidCreate(DIDCreate
                .builder()
                .build())
                .orElseThrow();
        assertNotNull(localDid2.getVerkey());
        Optional<LedgerDIDResponse> agent2Did = lc.ledgerDidCreate(
                LedgerDIDCreate
                    .builder()
                    .alias(alias_2)
                    .role(LedgerClient.LEDGER_DID_ENDORSER_ROLE)
                    .did(localDid2.getDid())
                    .verkey(localDid2.getVerkey())
                    .build());
        assertTrue(agent2Did.isPresent());
        assertNotNull(agent2Did.get().getDid());
        Optional<DID> localPublicDid2 = ac2.walletDidPublic(localDid2.getDid());
        assertTrue(localPublicDid2.isPresent());
        assertNotNull(localPublicDid2.get().getVerkey());

        // get (verify) public DID for each agent
        final Optional<DID> agent1PublicDid = ac.walletDidPublic();
        assertTrue(agent1PublicDid.isPresent());
        assertNotNull(agent1PublicDid.get().getDid());
        final Optional<DID> agent2PublicDid = ac2.walletDidPublic();
        assertTrue(agent2PublicDid.isPresent());
        assertNotNull(agent2PublicDid.get().getDid());

        // establish Endorser/Author connection for endorsements
        final Optional<CreateInvitationResponse> inv = ac.connectionsCreateInvitation(
                CreateInvitationRequest
                        .builder()
                        .recipientKeys(List.of())
                        .build(),
                CreateInvitationParams
                        .builder()
                        .autoAccept(Boolean.TRUE)
                        .alias(ENDORSER_CONNECTION_ALIAS)
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
                ConnectionFilter.builder().alias(ENDORSER_CONNECTION_ALIAS).build());
        assertTrue(connections.isPresent());
        assertEquals(1, connections.get().size());
        assertEquals(ENDORSER_CONNECTION_ALIAS, connections.get().get(0).getAlias());
        assertEquals(ConnectionState.ACTIVE, connections.get().get(0).getState());
        
        final Optional<List<ConnectionRecord>> connections2 = ac2.connections(
                ConnectionFilter.builder().alias(alias_2).build());
        assertTrue(connections2.isPresent());
        assertEquals(1, connections2.get().size());
        assertEquals(alias_2, connections2.get().get(0).getAlias());
        assertEquals(ConnectionState.ACTIVE, connections2.get().get(0).getState());

        // add endorser metadata to connections
        // TODO
    }
}
