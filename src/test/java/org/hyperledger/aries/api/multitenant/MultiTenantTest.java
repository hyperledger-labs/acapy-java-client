/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.multitenant;

import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.IntegrationTestBase;
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

import java.util.List;
import java.util.Optional;

@Testcontainers
public class MultiTenantTest {

    private final Logger log = LoggerFactory.getLogger(MultiTenantTest.class);
    private final String adminApiKey = "54321";
    private final String jwtSecret = "12345";

    @Container
    private final GenericContainer<?> ariesContainer = new GenericContainer<>(IntegrationTestBase.ARIES_VERSION)
            .withExposedPorts(IntegrationTestBase.ARIES_ADMIN_PORT)
            .withCommand("start"
                    + " -it http 0.0.0.0 8030"
                    + " -ot http --admin 0.0.0.0 " + IntegrationTestBase.ARIES_ADMIN_PORT
                    + " -e http://0.0.0.0"
                    + " --admin-api-key " + adminApiKey
                    + " --wallet-type indy"
                    + " --wallet-name multi"
                    + " --wallet-key secret"
                    + " --auto-provision"
                    + " --no-ledger"
                    + " --multitenant"
                    + " --jwt-secret " + jwtSecret
                    + " --multitenant-admin"
                    + " --log-level info")
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
        Optional<WalletRecord> walletRecord = base.multitenancyWalletCreate(CreateWalletRequest.builder()
                .walletKey("wallet1")
                .walletName("sub1")
                .label("mySubWallet1")
                .walletType(WalletType.INDY)
                .build());
        Assertions.assertTrue(walletRecord.isPresent());
        log.debug("{}", walletRecord.get());

        // test get sub wallets
        Optional<List<WalletRecord>> walletRecords = base.multitenancyWallets(null);
        Assertions.assertTrue(walletRecords.isPresent());
        log.debug("{}", walletRecords.get());
        Assertions.assertEquals(1, walletRecords.get().size());
        Assertions.assertEquals(walletRecord.get().getWalletId(), walletRecords.get().get(0).getWalletId());

        // test get wallet by wallet id
        Optional<WalletRecord> walletRecordReloaded = base.multitenancyWalletGet(walletRecord.get().getWalletId());
        Assertions.assertTrue(walletRecordReloaded.isPresent());
        Assertions.assertEquals(walletRecord.get().toString(), walletRecordReloaded.get().toString());

        // test update wallet
        Optional<WalletRecord> walletRecordUpdated = base.multitenancyWalletUpdate(walletRecord.get().getWalletId(),
                UpdateWalletRequest
                .builder()
                .walletWebhookUrls(List.of("http://localhost:9999/something/else"))
                .build());
        Assertions.assertTrue(walletRecordUpdated.isPresent());
        log.debug("{}", walletRecordUpdated.get());

        // test get new wallet token
        String walletKey = "MySecretKey123";
        Optional<CreateWalletTokenResponse> token = base.multitenancyWalletToken(walletRecord.get().getWalletId(),
                CreateWalletTokenRequest.builder()
                        .walletKey(walletKey)
                        .build());
        Assertions.assertTrue(token.isPresent());
        log.debug(token.get().getToken());

        // create client for sub wallet
        AriesClient subWallet = AriesClient.builder()
                .url(acaPyUrl)
                .apiKey(adminApiKey)
                .bearerToken(token.get().getToken())
                .build();
        // test random request to see if the sub client has the permission to do so
        subWallet.connections();

        // test delete wallet
        base.multitenancyWalletRemove(walletRecord.get().getWalletId(), RemoveWalletRequest.builder()
                .walletKey(walletKey)
                .build());

        // test delete success
        walletRecords = base.multitenancyWallets(null);
        Assertions.assertTrue(walletRecords.isPresent());
        Assertions.assertEquals(0, walletRecords.get().size());
    }
}
