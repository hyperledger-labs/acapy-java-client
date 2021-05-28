/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.ledger;

import org.hyperledger.acy_py.generated.model.DIDEndpointWithType;
import org.hyperledger.aries.IntegrationTestBase;
import org.hyperledger.aries.api.exception.AriesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LedgerTest extends IntegrationTestBase {

    @Test
    void testGetTaa() {
        Assertions.assertThrows(AriesException.class, ()-> ac.ledgerTaa());
    }

    @Test
    void testAcceptTaa() {
        Assertions.assertThrows(AriesException.class, ()-> ac.ledgerTaaAccept(new TAAAccept()));
    }

    @Test
    void testDidVerkey() {
        Assertions.assertThrows(AriesException.class, ()-> ac.ledgerDidVerkey("5mwQSWnRePrZ3oF67C4Kqe"));
    }

    @Test
    void testSetEndpoint() {
        AriesException e = Assertions.assertThrows(AriesException.class,
                () -> ac.ledgerDidEndpoint("DYqpVuxKRgiKvc5Bz8FWuZ", DIDEndpointWithType.EndpointTypeEnum.PROFILE));
        Assertions.assertTrue(e.getMessage().startsWith("No Indy"));
    }

}
