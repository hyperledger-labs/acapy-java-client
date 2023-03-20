/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.wallet;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class WalletFilterTest {

    @Test
    void testWalletCreateFilter() {
        AssignPublicDidFilter f = AssignPublicDidFilter.builder()
                .mediationId("1")
                .connId("2")
                .createTransactionForEndorser(Boolean.TRUE)
                .build();
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse( "https://localhost/wallet/did/public")).newBuilder();
        f.buildParams(b);
        Assertions.assertEquals("https://localhost/wallet/did/public?create_transaction_for_endorser=true&conn_id=2&mediation_id=1",
        b.toString());
    }
}
