/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import okhttp3.mockwebserver.MockResponse;
import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.api.jsonld.VerifiableCredential;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockedPresExTest extends MockedTestBase {

    @Test
    void testGetMatchingDifCredentials() throws Exception {
        String json = FileLoader.load("files/present-proof-v2/matching-dif-credential.json");
        server.enqueue(new MockResponse().setBody(json));

        final List<VerifiableCredential.VerifiableCredentialMatch> res = ac.presentProofV2RecordsCredentialsDif("1", null)
                .orElseThrow();

        assertEquals(1, res.size());
        Assertions.assertEquals("495e5da7fec34e2f90e5f4344a58035c", res.get(0).getRecordId());
        Assertions.assertEquals("did:sov:2BgZAEcbXcBfqW2R2VmFET", res.get(0).getIssuer());
    }
}
