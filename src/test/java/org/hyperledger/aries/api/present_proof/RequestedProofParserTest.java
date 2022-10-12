/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord.RevealedAttributeGroup;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.pojo.AttributeGroupName;
import org.hyperledger.aries.pojo.AttributeName;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

public class RequestedProofParserTest {

    private final Gson gson = GsonConfig.defaultConfig();

    @Test
    void testCollectRevealedAttributeGroups() {
        PresentationExchangeRecord ex = gson.fromJson(revealedGroups, PresentationExchangeRecord.class);
        Map<String, RevealedAttributeGroup> revealed = RequestedProofParser
                .collectAll(ex.getPresentation(), ex.getPresentationRequest());
        Assertions.assertEquals(1, revealed.size());
        RevealedAttributeGroup group = revealed.get("ba");
        Assertions.assertEquals(2, group.getRevealedAttributes().size());
        Assertions.assertEquals("111", group.getRevealedAttributes().get("bic"));
        Assertions.assertEquals(PresentationExchangeRecord.RequestedProofType.REVEALED_ATTR_GROUPS, group.getType());
        Assertions.assertTrue(group.getIdentifier().getSchemaId().startsWith("F6"));
    }

    @Test
    void testCollectRevealedAttributes() {
        String revealedAttr = FileLoader.load("events/proof-valid-verifier");
        PresentationExchangeRecord ex = gson.fromJson(revealedAttr, PresentationExchangeRecord.class);
        Map<String, RevealedAttributeGroup> revealed = RequestedProofParser
                .collectAll(ex.getPresentation(), ex.getPresentationRequest());

        Assertions.assertEquals(13, revealed.size());
        RevealedAttributeGroup group = revealed.get("10_email_uuid");
        Assertions.assertEquals(1, group.getRevealedAttributes().size());
        Assertions.assertEquals("info@test-corp.ch", group.getRevealedAttributes().get("email"));
        Assertions.assertEquals(PresentationExchangeRecord.RequestedProofType.REVEALED_ATTRS, group.getType());
        Assertions.assertTrue(group.getIdentifier().getSchemaId().startsWith("CHys"));
    }

    @Test
    void testCollectRevealedAttributesValues() {
        String revealedAttr = FileLoader.load("events/proof-valid-verifier");
        PresentationExchangeRecord ex = gson.fromJson(revealedAttr, PresentationExchangeRecord.class);
        Map<String, String> revealed = RequestedProofParser.collectRevealedAttributesValues(ex.getPresentation(),
                ex.getPresentationRequest());

        Assertions.assertEquals(13, revealed.size());
        Assertions.assertEquals("Zürich", revealed.get("city"));
    }

    @Test
    void testCollectPredicates() {
        PresentationExchangeRecord ex = gson.fromJson(predicates, PresentationExchangeRecord.class);
        Map<String, RevealedAttributeGroup> pred = RequestedProofParser
                .collectAll(ex.getPresentation(), ex.getPresentationRequest());

        Assertions.assertEquals(2, pred.size());
        RevealedAttributeGroup group = pred.get("iban-02");
        Assertions.assertEquals(0, group.getRevealedAttributes().size());
        Assertions.assertEquals(PresentationExchangeRecord.RequestedProofType.PREDICATES, group.getType());
        Assertions.assertTrue(group.getIdentifier().getSchemaId().startsWith("F6d"));
    }

    @Test
    void testCollectUnrevealed() {
        PresentationExchangeRecord ex = gson.fromJson(unrevealedAttribute, PresentationExchangeRecord.class);
        Map<String, RevealedAttributeGroup> unrev = RequestedProofParser
                .collectAll(ex.getPresentation(), ex.getPresentationRequest());

        Assertions.assertEquals(1, unrev.size());
        RevealedAttributeGroup group = unrev.get("account");
        Assertions.assertEquals(2, group.getRevealedAttributes().size());
        Assertions.assertEquals(PresentationExchangeRecord.RequestedProofType.UNREVEALED_ATTRS, group.getType());
        Assertions.assertTrue(group.getIdentifier().getSchemaId().startsWith("F6d"));
    }

    @Test
    void testCollectSelfAttested() {
        PresentationExchangeRecord ex = gson.fromJson(selfAttested, PresentationExchangeRecord.class);
        Map<String, RevealedAttributeGroup> selfAtt = RequestedProofParser
                .collectAll(ex.getPresentation(), ex.getPresentationRequest());

        Assertions.assertEquals(1, selfAtt.size());
        RevealedAttributeGroup group = selfAtt.get("some_iban");
        Assertions.assertEquals(1, group.getRevealedAttributes().size());
        Assertions.assertEquals("1234", group.getRevealedAttributes().get("iban"));
        Assertions.assertEquals(PresentationExchangeRecord.RequestedProofType.SELF_ATTESTED_ATTRS, group.getType());
        Assertions.assertNull(group.getIdentifier());
    }

    @Test
    void testCollectValuesFromSet() {
        String revealedAttr = FileLoader.load("events/proof-valid-verifier.json");
        PresentationExchangeRecord ex = gson.fromJson(revealedAttr, PresentationExchangeRecord.class);
        Map<String, Object> from = ex.from(Set.of("city", "street"));
        Assertions.assertEquals("Test Corp Campus", from.get("street"));
        Assertions.assertEquals("Zürich", from.get("city"));
    }

    @Test
    void testCollectValueFromAnnotatedClass() {
        String revealedAttr = FileLoader.load("events/proof-valid-verifier.json");
        PresentationExchangeRecord ex = gson.fromJson(revealedAttr, PresentationExchangeRecord.class);
        CountryNamed valueWrapped = ex.from(CountryNamed.class);
        Assertions.assertEquals("Switzerland", valueWrapped.getValue());
    }

    @Test
    void testCollectValueFromPojo() {
        String revealedAttr = FileLoader.load("events/proof-valid-verifier.json");
        PresentationExchangeRecord ex = gson.fromJson(revealedAttr, PresentationExchangeRecord.class);
        CountrySimple valueWrapped = ex.from(CountrySimple.class);
        Assertions.assertEquals("Switzerland", valueWrapped.getCountry());
    }

    @Data
    @NoArgsConstructor
    @AttributeGroupName("7_country_uuid")
    public static final class CountryNamed {
        @AttributeName("country")
        private String value;
    }

    @Data
    @NoArgsConstructor
    public static final class CountrySimple {
        private String country;
    }

    private final String revealedGroups = "{\n" +
            "    \"presentation\": {\n" +
            "        \"proof\": {},\n" +
            "        \"requested_proof\": {\n" +
            "            \"revealed_attrs\": {},\n" +
            "            \"revealed_attr_groups\": {\n" +
            "                \"ba\": {\n" +
            "                    \"sub_proof_index\": 0,\n" +
            "                    \"values\": {\n" +
            "                        \"bic\": {\n" +
            "                            \"raw\": \"111\"\n" +
            "                        },\n" +
            "                        \"iban\": {\n" +
            "                            \"raw\": \"222\"\n" +
            "                        }\n" +
            "                    }\n" +
            "                }\n" +
            "            },\n" +
            "            \"self_attested_attrs\": {},\n" +
            "            \"unrevealed_attrs\": {},\n" +
            "            \"predicates\": {}\n" +
            "        },\n" +
            "        \"identifiers\": [\n" +
            "            {\n" +
            "                \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\",\n" +
            "                \"cred_def_id\": \"DWMEL1dgePMxXzJrbsp5F:3:CL:18:ribbon-bank-01\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"presentation_request\": {\n" +
            "        \"requested_attributes\": {\n" +
            "            \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\": {\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"names\": [\n" +
            "                    \"bic\",\n" +
            "                    \"iban\"\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"requested_predicates\": {}\n" +
            "    }\n" +
            "}";

    private final String predicates = "{\n" +
            "    \"presentation\": {\n" +
            "        \"proof\": {},\n" +
            "        \"requested_proof\": {\n" +
            "            \"revealed_attrs\": {},\n" +
            "            \"self_attested_attrs\": {},\n" +
            "            \"unrevealed_attrs\": {},\n" +
            "            \"predicates\": {\n" +
            "                \"bic-01\": {\n" +
            "                    \"sub_proof_index\": 0\n" +
            "                },\n" +
            "                \"iban-02\": {\n" +
            "                    \"sub_proof_index\": 1\n" +
            "                }\n" +
            "            }\n" +
            "        },\n" +
            "        \"identifiers\": [\n" +
            "            {\n" +
            "                \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\",\n" +
            "                \"cred_def_id\": \"DWMEL1dgePMxXzJrbsp5F:3:CL:18:ribbon-bank-01\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\",\n" +
            "                \"cred_def_id\": \"F6dB7dMVHUQSC64qemnBi7:3:CL:18:phil-bank-01\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"presentation_request\": {\n" +
            "        \"requested_attributes\": {},\n" +
            "        \"requested_predicates\": {\n" +
            "            \"bic-01\": {\n" +
            "                \"p_value\": 1,\n" +
            "                \"name\": \"bic\",\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"p_type\": \">\"\n" +
            "            },\n" +
            "            \"iban-02\": {\n" +
            "                \"p_value\": 1,\n" +
            "                \"name\": \"iban\",\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"p_type\": \">\"\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}";

    private final String unrevealedAttribute = "{\n" +
            "    \"presentation\": {\n" +
            "        \"proof\": {},\n" +
            "        \"requested_proof\": {\n" +
            "            \"revealed_attrs\": {},\n" +
            "            \"self_attested_attrs\": {},\n" +
            "            \"unrevealed_attrs\": {\n" +
            "                \"account\": {\n" +
            "                    \"sub_proof_index\": 0\n" +
            "                }\n" +
            "            },\n" +
            "            \"predicates\": {}\n" +
            "        },\n" +
            "        \"identifiers\": [\n" +
            "            {\n" +
            "                \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\",\n" +
            "                \"cred_def_id\": \"DWMEL1dgePMxXzJrbsp5F:3:CL:18:ribbon-bank-01\"\n" +
            "            }\n" +
            "        ]\n" +
            "    },\n" +
            "    \"presentation_request\": {\n" +
            "        \"requested_attributes\": {\n" +
            "            \"account\": {\n" +
            "                \"restrictions\": [\n" +
            "                    {\n" +
            "                        \"schema_id\": \"F6dB7dMVHUQSC64qemnBi7:2:bank_account:1.0\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"names\": [\n" +
            "                    \"bic\",\n" +
            "                    \"iban\"\n" +
            "                ]\n" +
            "            }\n" +
            "        },\n" +
            "        \"requested_predicates\": {}\n" +
            "    }\n" +
            "}";

    private final String selfAttested = "{\n" +
            "    \"presentation\": {\n" +
            "        \"proof\": {},\n" +
            "        \"requested_proof\": {\n" +
            "            \"revealed_attrs\": {},\n" +
            "            \"self_attested_attrs\": {\n" +
            "                \"some_iban\": \"1234\"\n" +
            "            },\n" +
            "            \"unrevealed_attrs\": {},\n" +
            "            \"predicates\": {}\n" +
            "        },\n" +
            "        \"identifiers\": []\n" +
            "    },\n" +
            "    \"presentation_request\": {\n" +
            "        \"nonce\": \"262033222642465746728714\",\n" +
            "        \"name\": \"test self attested attributes\",\n" +
            "        \"version\": \"0.1\",\n" +
            "        \"requested_attributes\": {\n" +
            "            \"some_iban\": {\n" +
            "                \"restrictions\": [],\n" +
            "                \"name\": \"iban\"\n" +
            "            }\n" +
            "        },\n" +
            "        \"requested_predicates\": {}\n" +
            "    }\n" +
            "}";
}
