/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import com.google.gson.Gson;
import org.hyperledger.aries.api.jsonld.VerifiableCredential;
import org.hyperledger.aries.api.jsonld.VerifiablePresentation;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DifPresExTest {

    private final Gson gson = GsonConfig.defaultConfig();

    @Test
    void testParsePresentationRequest() {
        String json = FileLoader.load("files/present-proof-v2/verifier-done-dif.json");
        V20PresExRecord v2 = gson.fromJson(json, V20PresExRecord.class);

        Assertions.assertTrue(v2.isDif());
        V2DIFProofRequest pr =
                v2.resolveDifPresentationRequest();
        Assertions.assertNotNull(pr);
        Assertions.assertEquals("https://w3id.org/citizenship#PermanentResident",
                pr.getPresentationDefinition().getInputDescriptors().get(0).getSchema().get(1).getUri());


        VerifiablePresentation<VerifiableCredential> vp = v2.resolveDifPresentation();
        Assertions.assertNotNull(vp);
        Assertions.assertEquals("Camille", vp.getVerifiableCredential().get(0).getCredentialSubject().get("name").getAsString());
    }

    @Test
    void testParsePresentation() {
        String json = FileLoader.load("files/present-proof-v2/verifier-proposal-received.json");
        V20PresExRecord v2 = gson.fromJson(json, V20PresExRecord.class);

        V20PresProposalByFormat.DIFProofProposal presDef = v2.resolveDifPresentationProposal();
        Assertions.assertEquals(1, presDef.getInputDescriptors().size());
        Assertions.assertEquals(2, presDef.getInputDescriptors().get(0).getConstraints().getFields().size());
    }
}
