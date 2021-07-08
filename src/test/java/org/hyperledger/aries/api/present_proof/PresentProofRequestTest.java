/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PresentProofRequestTest {

    @Test
    public void testRestrictionWithSchemaIdAndIssuer(){
        String sut = PresentProofRequest.ProofRequest.ProofRestrictions.builder()
                .issuerDid("issuerId")
                .schemaId("schemaId")
                .build()
                .toJsonObject()
                .toString();
        Assertions.assertEquals("{\"schema_id\":\"schemaId\",\"issuer_did\":\"issuerId\"}", sut);
    }

    @Test
    public void testAttributeValueWithPostProcessing(){
        JsonObject obj = PresentProofRequest.ProofRequest.ProofRestrictions.builder()
                .issuerDid("issuerId")
                .schemaId("schemaId")
                .build()
                .toJsonObject();
        String sut = PresentProofRequest.ProofRequest.ProofRestrictions.addNameValue("attrName", "value", obj).toString();
        Assertions.assertEquals("{\"schema_id\":\"schemaId\"," +
                "\"issuer_did\":\"issuerId\"," +
                "\"attr::attrName::value\":\"value\"}", sut);
    }

    @Test
    public void testAttributeValueWithBuilderFunction(){
        String sut = PresentProofRequest.ProofRequest.ProofRestrictions.builder()
                .issuerDid("issuerId")
                .schemaId("schemaId")
                .addAttributeValueRestriction("attrName", "value")
                .build()
                .toJsonObject()
                .toString();
        Assertions.assertEquals("{\"schema_id\":\"schemaId\"," +
                "\"issuer_did\":\"issuerId\"," +
                "\"attr::attrName::value\":\"value\"}", sut);
    }
}
