/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
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
import org.hyperledger.aries.api.exception.AriesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@Slf4j
public class WalletTest extends IntegrationTestBase {

    @Test
    void testCreateAndListWalletDids() throws Exception {

        // as the wallet is empty by default create local did first
        DID localDid = ac.walletDidCreate(DIDCreate
                .builder()
                .build())
                .orElseThrow();
        Assertions.assertNotNull(localDid.getVerkey());

        // list all did's
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
        DID localDid = ac.walletDidCreate(DIDCreate.builder().build()).orElseThrow();

        final String url = "http://localhost:8031";
        DIDEndpointWithType req = DIDEndpointWithType
                .builder()
                .endpoint(url)
                .did(localDid.getDid())
                .build();
        ac.walletSetDidEndpoint(req);

        DIDEndpoint endp = ac.walletGetDidEndpoint(localDid.getDid()).orElseThrow();
        Assertions.assertEquals(url, endp.getEndpoint());
    }

    @Test
    void TestRotateKeypair() throws Exception{
        DID localDid = ac.walletDidCreate(DIDCreate
                .builder()
                .build()).orElseThrow();
        Assertions.assertNotNull(localDid.getVerkey());

        ac.walletDidLocalRotateKeypair(localDid.getDid()); // returns only 200
    }

    @Test
    void testRotateKeypairWrongDid() {
        Assertions.assertThrows(AriesException.class, () -> ac.walletDidLocalRotateKeypair("WgWxqztrNooG92RXvxSTWx"));
    }
}
