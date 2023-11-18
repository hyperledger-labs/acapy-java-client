/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.multitenant;

import org.hyperledger.acy_py.generated.model.ConnectionInvitation;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.acy_py.generated.model.DIDCreate;
import org.hyperledger.acy_py.generated.model.DIDEndpointWithType;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.ClientToTenant;
import org.hyperledger.aries.IntegrationTestBase;
import org.hyperledger.aries.api.connection.*;
import org.hyperledger.aries.api.multitenancy.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

@Testcontainers
public class MultiTenantTest {

    private final Logger log = LoggerFactory.getLogger(MultiTenantTest.class);
    private final String adminApiKey = "54321";
    private final String jwtSecret = "12345";

    @Container
    private final GenericContainer<?> ariesContainer = new GenericContainer<>(IntegrationTestBase.ARIES_VERSION)
            .withExposedPorts(IntegrationTestBase.ARIES_INBOUND_PORT, IntegrationTestBase.ARIES_ADMIN_PORT)
            .withCommand("start"
                    + " -it http 0.0.0.0 " + IntegrationTestBase.ARIES_INBOUND_PORT
                    + " -ot http --admin 0.0.0.0 " + IntegrationTestBase.ARIES_ADMIN_PORT
                    + " -e http://0.0.0.0:" + IntegrationTestBase.ARIES_INBOUND_PORT
                    + " --admin-api-key " + adminApiKey
                    + " --wallet-type askar"
                    + " --wallet-name multi"
                    + " --wallet-key secret"
                    + " --auto-provision"
                    + " --no-ledger"
                    + " --auto-ping-connection"
                    + " --multitenant"
                    + " --jwt-secret " + jwtSecret
                    + " --multitenant-admin"
                    + " --log-level error")
            .waitingFor(Wait.defaultWaitStrategy())
            .withLogConsumer(new Slf4jLogConsumer(log))
            ;

    @Test
    void testMultiFlow() throws Exception {
        String acaPyUrl = "http://localhost:" + ariesContainer.getMappedPort(IntegrationTestBase.ARIES_ADMIN_PORT);

        AriesClient base = AriesClient.builder()
                .url(acaPyUrl)
                .apiKey(adminApiKey)
                .build();

        // test create
        WalletRecord wallet1 = base.multitenancyWalletCreate(CreateWalletRequest.builder()
                .walletKey("wallet1")
                .walletName("wallet1")
                .label("mySubWallet1")
                .walletType(WalletType.ASKAR)
                .build()).orElseThrow();
        log.debug("{}", wallet1);

        // test get sub wallets
        List<WalletRecord> walletRecords = base.multitenancyWallets(null).orElseThrow();
        log.debug("{}", walletRecords);
        Assertions.assertEquals(1, walletRecords.size());
        Assertions.assertEquals(wallet1.getWalletId(), walletRecords.get(0).getWalletId());

        // test get wallet by wallet id
        WalletRecord walletRecordReloaded = base.multitenancyWalletGet(wallet1.getWalletId()).orElseThrow();
        Assertions.assertEquals(wallet1.toString(), walletRecordReloaded.toString());

        // test update wallet
        WalletRecord walletRecordUpdated = base.multitenancyWalletUpdate(wallet1.getWalletId(),
                UpdateWalletRequest
                .builder()
                .walletWebhookUrls(List.of("http://localhost:9999/something/else"))
                .build())
                .orElseThrow();
        log.debug("{}", walletRecordUpdated);

        // test get new wallet token
        String walletKey = "MySecretKey123";
        CreateWalletTokenResponse token = base.multitenancyWalletToken(wallet1.getWalletId(),
                CreateWalletTokenRequest.builder()
                        .walletKey(walletKey)
                        .build())
                .orElseThrow();
        log.debug(token.getToken());

        // create client for sub wallet
        AriesClient sub1 = AriesClient.builder()
                .url(acaPyUrl)
                .apiKey(adminApiKey)
                .bearerToken(token.getToken())
                .build();
        // test random request to see if the sub client has the permission to do so
        sub1.connections();

        // test delete wallet
        base.multitenancyWalletRemove(wallet1.getWalletId(), RemoveWalletRequest.builder()
                .walletKey(walletKey)
                .build());

        // test delete success
        walletRecords = base.multitenancyWallets(null).orElseThrow();
        Assertions.assertEquals(0, walletRecords.size());
    }

    @Test
    void testConnectSubWallets() throws Exception {
        String adminUrl = "http://localhost:" + ariesContainer.getMappedPort(IntegrationTestBase.ARIES_ADMIN_PORT);
        String endpointUrl = "http://localhost:" + IntegrationTestBase.ARIES_INBOUND_PORT;

        AriesClient base = AriesClient.builder()
                .url(adminUrl)
                .apiKey(adminApiKey)
                .build();

        // test create
        ClientToTenant wallet1 = base.multitenancyWalletCreateWithClient(CreateWalletRequest.builder()
                .walletKey("wallet1")
                .walletName("wallet1")
                .label("mySubWallet1")
                .walletType(WalletType.ASKAR)
                .walletDispatchType(WalletDispatchType.BOTH)
                .build());
        log.debug("wallet 1: {}", wallet1);

        ClientToTenant wallet2 = base.multitenancyWalletCreateWithClient(CreateWalletRequest.builder()
                .walletKey("wallet2")
                .walletName("wallet2")
                .label("mySubWallet2")
                .walletType(WalletType.ASKAR)
                .walletDispatchType(WalletDispatchType.BOTH)
                .build());
        log.debug("wallet 2: {}", wallet2);

        // test get sub wallets
        List<WalletRecord> walletRecords = base.multitenancyWallets(null).orElseThrow();
        Assertions.assertEquals(2, walletRecords.size());

        // prepare the wallets

        DID did1 = wallet1.send().walletDidCreate(DIDCreate.builder().build()).orElseThrow();

        // If running against a public ledger
        // sub1.walletDidPublic(did1.getDid());
        wallet1.send().walletSetDidEndpoint(DIDEndpointWithType
                .builder()
                .did(did1.getDid())
                .endpoint(endpointUrl)
                .endpointType(DIDEndpointWithType.EndpointTypeEnum.ENDPOINT)
                .build());

        DID did2 = wallet2.send().walletDidCreate(DIDCreate.builder().build()).orElseThrow();

        // If running against a public ledger
        // sub2.walletDidPublic(did2.getDid());
        wallet2.send().walletSetDidEndpoint(DIDEndpointWithType
                .builder()
                .did(did2.getDid())
                .endpoint(endpointUrl)
                .endpointType(DIDEndpointWithType.EndpointTypeEnum.ENDPOINT)
                .build());

        // Connect sub wallet 2 with sub wallet 1

        // wallet 2 creates invitation
        CreateInvitationResponse sub2Invite = wallet2.send().connectionsCreateInvitation(CreateInvitationRequest
                .builder().build()).orElseThrow();
        ConnectionInvitation invitation2 = sub2Invite.getInvitation();
        log.debug("Invitation wallet-2: {}", sub2Invite);

        // wallet 1 receives the invitation
        ConnectionRecord cr1 = wallet1.send().connectionsReceiveInvitation(ReceiveInvitationRequest.builder()
                        .serviceEndpoint(invitation2.getServiceEndpoint())
                        .recipientKeys(invitation2.getRecipientKeys())
                        .build(), ConnectionReceiveInvitationFilter.builder().autoAccept(true).build())
                .orElseThrow();
        log.debug("connection record wallet-1: {}", cr1);

        wallet2.receive().connection().filter(ConnectionRecord::stateIsRequest).subscribe(s -> {
            log.debug("wallet-2 accepting connection request: {}", s);
            try {
                wallet2.send().connectionsAcceptRequest(s.getConnectionId(), null);
            } catch (IOException e) {
                Assertions.fail("expecting to connect", e);
            }
        });

        ConnectionRecord active = wallet2.receive().connection()
                .filter(ConnectionRecord::stateIsActive)
                .blockFirst(Duration.ofSeconds(5));
        Assertions.assertNotNull(active);
        Assertions.assertEquals(cr1.getInvitationKey(), active.getInvitationKey());

        // test delete wallet
        base.multitenancyWalletRemove(wallet1.getWalletId(), RemoveWalletRequest.builder()
                .walletKey(wallet1.getToken())
                .build());
        base.multitenancyWalletRemove(wallet2.getWalletId(), RemoveWalletRequest.builder()
                .walletKey(wallet2.getToken())
                .build());

        // test delete success
        walletRecords = base.multitenancyWallets(null).orElseThrow();
        Assertions.assertEquals(0, walletRecords.size());

        wallet1.close();
        wallet2.close();
    }
}
