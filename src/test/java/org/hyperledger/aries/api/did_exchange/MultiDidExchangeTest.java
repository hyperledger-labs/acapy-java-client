/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.did_exchange;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.acy_py.generated.model.DIDCreate;
import org.hyperledger.aries.MultiIntegrationTestBase;
import org.hyperledger.aries.api.connection.ConnectionAcceptRequestFilter;
import org.hyperledger.aries.api.connection.ConnectionFilter;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.connection.ConnectionState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.hyperledger.aries.LedgerClient;
import org.hyperledger.aries.util.ledger.LedgerDIDCreate;
import org.hyperledger.aries.util.ledger.LedgerDIDResponse;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MultiDidExchangeTest extends MultiIntegrationTestBase {

    public static final String INDY_LEDGER_URL = "https://indy-test.bosch-digital.de";

    protected String getLedgerUrl() {
        return INDY_LEDGER_URL;
    }

    protected String extraAgentArgs(int agentNum) {
        switch (agentNum) {
            case 1:
                return " --genesis-url https://indy-test.bosch-digital.de/genesis"
                     + " --auto-ping-connection"
                     + " --public-invites"
                     + " --wallet-type indy"
                     + " --wallet-name agent1"
                     + " --wallet-key agent1key"
                     + " --auto-provision";
            case 2:
                return " --genesis-url https://indy-test.bosch-digital.de/genesis"
                     + " --auto-ping-connection"
                     + " --public-invites"
                     + " --wallet-type indy"
                     + " --wallet-name agent2"
                     + " --wallet-key agent2key"
                     + " --auto-provision";
            default:
                return super.extraAgentArgs(agentNum);
        }
    }

    @Test
    void testCreateRequest() throws Exception {
        String alias_1 = "my_didex_test_1";
        String alias_2 = "my_didex_test_2";

        // create a DID for one agent and make it public
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

        final Optional<DID> agent1PublicDid = ac.walletDidPublic();
        assertTrue(agent1PublicDid.isPresent());
        assertNotNull(agent1PublicDid.get().getDid());
        String theirDid = agent1PublicDid.get().getDid();

        // agent 2 makes an implicit connection request using agent 1's public DID
        // TODO include the "alias" parameter when available in aca-py
        final Optional<ConnectionRecord> c = ac2.didExchangeCreateRequest(DidExchangeCreateRequestFilter
                .builder()
                .theirPublicDid(theirDid)
                .build());
        Assertions.assertTrue(c.isPresent());
        Assertions.assertEquals(ConnectionRecord.ConnectionProtocol.DID_EXCHANGE_V1, c.get().getConnectionProtocol());

        // wait for the connection to complete and then verify each agent has an active connection
        Thread.sleep(1000);
        
        // now fetch connections and confirm everything completed successfully
        // TODO include the "alias" parameter when available in aca-py
        final Optional<List<ConnectionRecord>> connections = ac.connections(
                ConnectionFilter.builder()/*.alias(alias_1)*/.build());
        assertTrue(connections.isPresent());
        assertEquals(1, connections.get().size());
        //assertEquals(alias_1, connections.get().get(0).getAlias());
        assertEquals(ConnectionState.REQUEST, connections.get().get(0).getState());

        // accept request (can't "auto"?)
        final Optional<ConnectionRecord> connection1 = ac.connectionsAcceptRequest(
                connections.get().get(0).getConnectionId(),
                ConnectionAcceptRequestFilter.builder().build());
        Thread.sleep(2000);

        final Optional<List<ConnectionRecord>> connections2 = ac2.connections(
                ConnectionFilter.builder()/*.alias(alias_2)*/.build());
        assertTrue(connections2.isPresent());
        assertEquals(1, connections2.get().size());
        //assertEquals(alias_2, connections2.get().get(0).getAlias());
        assertEquals(ConnectionState.ACTIVE, connections2.get().get(0).getState());
    }
}
