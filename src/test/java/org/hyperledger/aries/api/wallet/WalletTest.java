/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.wallet;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.acy_py.generated.model.DIDCreate;
import org.hyperledger.acy_py.generated.model.DIDEndpoint;
import org.hyperledger.acy_py.generated.model.DIDEndpointWithType;
import org.hyperledger.aries.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@Slf4j
public class WalletTest extends IntegrationTestBase {

    @Test
    void testCreateAndListWalletDids() throws Exception {

        // as the wallet is empty by default create local did first
        final Optional<DID> localDid = ac.walletDidCreate(DIDCreate
                .builder()
                .build());
        Assertions.assertTrue(localDid.isPresent());
        Assertions.assertNotNull(localDid.get().getVerkey());

        // list all dids
        final Optional<List<DID>> walletDid = ac.walletDid();
        Assertions.assertTrue(walletDid.isPresent());
        Assertions.assertEquals(1, walletDid.get().size());
        walletDid.get().forEach(did -> log.debug("{}", did));
    }

    @Test
    void testGetPublicDid() throws Exception {
        final Optional<DID> publicDid = ac.walletDidPublic();
        Assertions.assertTrue(publicDid.isEmpty());
    }

    @Test
    void testSetGetDidEndpoint() throws Exception {
        final Optional<DID> localDid = ac.walletDidCreate(DIDCreate.builder().build());
        Assertions.assertTrue(localDid.isPresent());

        final String url = "http://localhost:8031";
        DIDEndpointWithType req = DIDEndpointWithType
                .builder()
                .endpoint(url)
                .did(localDid.get().getDid())
                .build();
        ac.walletSetDidEndpoint(req);

        final Optional<DIDEndpoint> endp = ac.walletGetDidEndpoint(localDid.get().getDid());
        Assertions.assertTrue(endp.isPresent());
        Assertions.assertEquals(url, endp.get().getEndpoint());
    }
}
