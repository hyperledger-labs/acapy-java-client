/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.google.gson.Gson;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

public class SendPresentationRequestHelperTest {

    private final Gson gson = GsonConfig.defaultConfig();

    @Test
    void testSingleMatch() {
        String referent = UUID.randomUUID().toString();
        PresentationExchangeRecord ex = gson.fromJson(presExSingle, PresentationExchangeRecord.class);

        SendPresentationRequest sendPresentationReq = SendPresentationRequestHelper
                .buildRequest(ex, Map.of("gr_0", SendPresentationRequestHelper.SelectedMatch.ReferentInfo.builder()
                        .referent(referent)
                        .build()));

        Assertions.assertNotNull(sendPresentationReq.getRequestedAttributes());
        SendPresentationRequest.IndyRequestedCredsRequestedAttr reqAttr = sendPresentationReq.getRequestedAttributes()
                .get("gr_0");
        Assertions.assertNotNull(reqAttr);
        Assertions.assertEquals(referent, reqAttr.getCredId());
        Assertions.assertTrue(reqAttr.getRevealed());
        Assertions.assertNotNull(sendPresentationReq.getRequestedPredicates());
        Assertions.assertNotNull(sendPresentationReq.getSelfAttestedAttributes());

        // System.out.println(GsonConfig.prettyPrinter().toJson(sendPresentationReq));
    }

    @Test
    void testMultipleMatches() {
        String referentGr0 = UUID.randomUUID().toString();
        String referentGr1 = UUID.randomUUID().toString();
        PresentationExchangeRecord ex = gson.fromJson(presExMulti, PresentationExchangeRecord.class);

        SendPresentationRequest sendPresentationReq = SendPresentationRequestHelper
                .buildRequest(ex, Map.of(
                        "gr_0", SendPresentationRequestHelper.SelectedMatch.ReferentInfo.builder()
                                .referent(referentGr0).build(),
                        "gr_1", SendPresentationRequestHelper.SelectedMatch.ReferentInfo.builder()
                                .referent(referentGr1).revealed(Boolean.FALSE).build()));

        Assertions.assertEquals(2, sendPresentationReq.getRequestedAttributes().size());

        SendPresentationRequest.IndyRequestedCredsRequestedAttr reqAttrGr0 = sendPresentationReq.getRequestedAttributes()
                .get("gr_0");
        Assertions.assertNotNull(reqAttrGr0);
        Assertions.assertEquals(referentGr0, reqAttrGr0.getCredId());
        Assertions.assertTrue(reqAttrGr0.getRevealed());

        SendPresentationRequest.IndyRequestedCredsRequestedAttr reqAttrGr1 = sendPresentationReq.getRequestedAttributes()
                .get("gr_1");
        Assertions.assertNotNull(reqAttrGr1);
        Assertions.assertEquals(referentGr1, reqAttrGr1.getCredId());
        Assertions.assertFalse(reqAttrGr1.getRevealed());

        Assertions.assertNotNull(sendPresentationReq.getRequestedPredicates());
        Assertions.assertNotNull(sendPresentationReq.getSelfAttestedAttributes());

        // System.out.println(GsonConfig.prettyPrinter().toJson(sendPresentationReq));
    }

    @Test
    void testMultipleWithSelfAttested() {
        String referentGr0 = UUID.randomUUID().toString();
        PresentationExchangeRecord ex = gson.fromJson(presExMulti, PresentationExchangeRecord.class);

        SendPresentationRequest sendPresentationReq = SendPresentationRequestHelper
                .buildRequest(ex, Map.of(
                        "gr_0", SendPresentationRequestHelper.SelectedMatch.ReferentInfo.builder()
                                .referent(referentGr0).build(),
                        "gr_1", SendPresentationRequestHelper.SelectedMatch.ReferentInfo.builder()
                                .selfAttestedValue("self").build()));

        Assertions.assertEquals(1, sendPresentationReq.getRequestedAttributes().size());
        SendPresentationRequest.IndyRequestedCredsRequestedAttr reqAttrGr0 = sendPresentationReq.getRequestedAttributes()
                .get("gr_0");
        Assertions.assertNotNull(reqAttrGr0);
        Assertions.assertEquals(referentGr0, reqAttrGr0.getCredId());
        Assertions.assertTrue(reqAttrGr0.getRevealed());

        Assertions.assertEquals(1, sendPresentationReq.getSelfAttestedAttributes().size());
        String selfAttested = sendPresentationReq.getSelfAttestedAttributes().get("gr_1");
        Assertions.assertNotNull(selfAttested);
        Assertions.assertEquals("self", selfAttested);

        Assertions.assertNotNull(sendPresentationReq.getRequestedPredicates());

        // System.out.println(GsonConfig.prettyPrinter().toJson(sendPresentationReq));
    }

    @Test
    void testNoMatchingCredentialFound() {
        PresentationExchangeRecord ex = gson.fromJson(presExMulti, PresentationExchangeRecord.class);
        SendPresentationRequest presentationRequest = SendPresentationRequestHelper.buildRequest(ex, Map.of());
        Assertions.assertNotNull(presentationRequest);
    }

    @Test
    void testWithPredicates() {
        String referentGr0 = UUID.randomUUID().toString();
        String referentGr1 = UUID.randomUUID().toString();
        PresentationExchangeRecord ex = gson.fromJson(presExPredicates, PresentationExchangeRecord.class);

        SendPresentationRequest sendPresentationReq = SendPresentationRequestHelper
                .buildRequest(ex, Map.of(
                        "gr_0", SendPresentationRequestHelper.SelectedMatch.ReferentInfo.builder()
                                .referent(referentGr0).build(),
                        "pr_0", SendPresentationRequestHelper.SelectedMatch.ReferentInfo.builder()
                                .referent(referentGr1).build()));

        Assertions.assertEquals(1, sendPresentationReq.getRequestedAttributes().size());
        SendPresentationRequest.IndyRequestedCredsRequestedAttr reqAttrGr0 = sendPresentationReq.getRequestedAttributes()
                .get("gr_0");
        Assertions.assertNotNull(reqAttrGr0);
        Assertions.assertEquals(referentGr0, reqAttrGr0.getCredId());
        Assertions.assertTrue(reqAttrGr0.getRevealed());

        Assertions.assertEquals(1, sendPresentationReq.getRequestedPredicates().size());
        SendPresentationRequest.IndyRequestedCredsRequestedPred reqPredGr0 = sendPresentationReq.getRequestedPredicates()
                .get("pr_0");
        Assertions.assertNotNull(reqPredGr0);
        Assertions.assertEquals(referentGr1, reqPredGr0.getCredId());

        // System.out.println(GsonConfig.prettyPrinter().toJson(sendPresentationReq));
    }

    private final String presExSingle = "    {\n" +
            "      \"presentation_request\": {\n" +
            "        \"name\": \"Proof request\",\n" +
            "        \"version\": \"1.0\",\n" +
            "        \"requested_attributes\": {\n" +
            "          \"gr_0\": {\n" +
            "            \"names\": [\n" +
            "              \"bic\",\n" +
            "              \"iban\"\n" +
            "            ],\n" +
            "            \"restrictions\": [\n" +
            "              {\n" +
            "                \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\"\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        },\n" +
            "        \"requested_predicates\": {},\n" +
            "        \"nonce\": \"1046094633050809199221859\"\n" +
            "      },\n" +
            "      \"updated_at\": \"2021-05-31 15:09:24.971834Z\",\n" +
            "      \"trace\": false,\n" +
            "      \"presentation_exchange_id\": \"9bdba6ce-115e-45ab-8b10-7ef1ab1003ed\",\n" +
            "      \"state\": \"request_received\",\n" +
            "      \"thread_id\": \"689e2ab1-4168-446a-902f-81ceaec16412\"\n" +
            "    }";

    private final String presExMulti = "{\n" +
            "    \"connection_id\": \"8bc1ccef-7ae3-40e9-90bf-12d3228856d3\",\n" +
            "    \"thread_id\": \"c1695bbc-7fed-4e49-b848-67f56f31b6c5\",\n" +
            "    \"role\": \"prover\",\n" +
            "    \"presentation_exchange_id\": \"2b72e261-b7c1-4ae9-832a-991aa10d9c3f\",\n" +
            "    \"state\": \"request_received\",\n" +
            "    \"presentation_request\": {\n" +
            "        \"name\": \"Proof request\",\n" +
            "        \"version\": \"1.0\",\n" +
            "        \"requested_attributes\": {\n" +
            "            \"gr_0\": {\n" +
            "                \"names\": [\n" +
            "                    \"iban\"\n" +
            "                ],\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            \"gr_1\": {\n" +
            "                \"names\": [\n" +
            "                    \"firstName\"\n" +
            "                ],\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"847fVkFJiNZ4FUew9g6Zn4:2:Basis-ID:1.0\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"requested_predicates\": {},\n" +
            "        \"nonce\": \"557987630597052052548096\"\n" +
            "    }\n" +
            "}";

    private final String presExPredicates = "{\n" +
            "    \"thread_id\": \"673e93b5-eb38-42cf-a1e7-7861563ef7c6\",\n" +
            "    \"created_at\": \"2021-06-02 12:41:32.643524Z\",\n" +
            "    \"initiator\": \"external\",\n" +
            "    \"trace\": false,\n" +
            "    \"updated_at\": \"2021-06-02 12:41:32.643524Z\",\n" +
            "    \"connection_id\": \"8bc1ccef-7ae3-40e9-90bf-12d3228856d3\",\n" +
            "    \"role\": \"prover\",\n" +
            "    \"presentation_request\": {\n" +
            "        \"name\": \"Proof request\",\n" +
            "        \"version\": \"1.0\",\n" +
            "        \"requested_attributes\": {\n" +
            "            \"gr_0\": {\n" +
            "                \"name\": \"iban\",\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"requested_predicates\": {\n" +
            "            \"pr_0\": {\n" +
            "                \"name\": \"phone\",\n" +
            "                \"p_type\": \">=\",\n" +
            "                \"p_value\": 20\n" +
            "            }\n" +
            "        },\n" +
            "        \"nonce\": \"557987630597052052548096\"\n" +
            "    },\n" +
            "    \"presentation_exchange_id\": \"e6e808b3-327e-4b1f-9917-41ece901a3c3\",\n" +
            "    \"state\": \"request_received\"\n" +
            "}";
}
