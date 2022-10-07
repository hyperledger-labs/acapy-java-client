/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.connection.ConnectionState;
import org.hyperledger.aries.api.discover_features.DiscoverFeatureEvent;
import org.hyperledger.aries.api.issue_credential_v1.CredentialExchangeRole;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.api.message.ProblemReport;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRole;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.pojo.AttributeName;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

public class EventParserTest {

    private final EventParser parser = new EventParser();

    @Test
    void testParseConnectionEvent() {
        String json = FileLoader.load("events/connection-active.json");
        ConnectionRecord conn = parser.parseValueSave(json, ConnectionRecord.class).orElseThrow();
        Assertions.assertEquals(ConnectionState.ACTIVE, conn.getState());
    }

    @Test
    void testParseIssuedCredential() {
        String json = FileLoader.load("events/issue-credential.json");
        V1CredentialExchange ex = parser.parseValueSave(json, V1CredentialExchange.class).orElseThrow();
        Assertions.assertEquals(CredentialExchangeRole.HOLDER, ex.getRole());
        Assertions.assertNotNull(ex.getCredentialDefinitionId());
        Assertions.assertNotNull(ex.getCredential());
    }

    @Test
    void testParseProofPresentationVerifier() {
        String json = FileLoader.load("events/proof-valid-verifier.json");
        PresentationExchangeRecord p = parser.parsePresentProof(json).orElseThrow();
        Assertions.assertEquals(PresentationExchangeRole.VERIFIER, p.getRole());
        Masterdata md = p.from(Masterdata.class);
        Assertions.assertEquals("4", md.getStreetNumber());
        Assertions.assertEquals("8000", md.getPostalCode());
        Assertions.assertNotNull(p.getIdentifiers());
        Assertions.assertEquals(1, p.getIdentifiers().size());
        Assertions.assertTrue(p.getIdentifiers().get(0).getSchemaId().startsWith("CHysca6fY8n8ytCDLAJGZj"));
        Assertions.assertTrue(p.getIdentifiers().get(0).getCredentialDefinitionId().startsWith("CHysca6fY8n8ytCDLAJGZj"));
    }

    @Test
    void testParseProofPresentationVerifierMap() {
        String json = FileLoader.load("events/proof-valid-verifier.json");
        PresentationExchangeRecord p = parser.parsePresentProof(json).orElseThrow();
        Map<String, Object> md = p.from(Set.of("country", "city"));
        Assertions.assertEquals("Switzerland", md.get("country"));
        Assertions.assertEquals("Zürich", md.get("city"));
    }

    @Test
    void testProofPresentationGetRevealedAttributeGroups() {
        String json = FileLoader.load("events/proof-valid-verifier-attr-group.json");
        PresentationExchangeRecord p = parser.parsePresentProof(json).orElseThrow();
        Map<String, PresentationExchangeRecord.RevealedAttributeGroup> revealedAttributesByGroup = p.findRevealedAttributeGroups();
        System.out.println(GsonConfig.prettyPrinter().toJson(revealedAttributesByGroup));
        Assertions.assertEquals("1234", revealedAttributesByGroup.get("bank-account").getRevealedAttributes().get("bic"));
    }

    @Test
    void testProofPresentationGetRevealedAttributes() {
        String json = FileLoader.load("events/proof-valid-verifier.json");
        PresentationExchangeRecord p = parser.parsePresentProof(json).orElseThrow();
        Map<String, String> attrs = p.findRevealedAttributes();
        Assertions.assertEquals("Zürich", attrs.get("city"));
    }

    @Test
    void testParseProofPresentationProver() {
        String json = FileLoader.load("events/proof-valid-prover.json");
        PresentationExchangeRecord p = parser.parsePresentProof(json).orElseThrow();
        Assertions.assertEquals(PresentationExchangeRole.PROVER, p.getRole());
        final BankAccount ba = p.from(BankAccount.class);
        Assertions.assertNotNull(ba);
        Assertions.assertEquals("GB33BUKB20201555555555", ba.getIban());
        Assertions.assertEquals("PBNK", ba.getBic());
    }

    @Test
    void testParseProofPresentationProverMap() {
        String json = FileLoader.load("events/proof-valid-prover.json");
        PresentationExchangeRecord p = parser.parsePresentProof(json).orElseThrow();
        final Map<String, Object> ba = p.from(Set.of("iban", "bic"));
        Assertions.assertNotNull(ba);
        Assertions.assertEquals("GB33BUKB20201555555555", ba.get("iban"));
        Assertions.assertEquals("PBNK", ba.get("bic"));
    }

    @Test
    void testParseDiscoverFeature() {
        String jsonIn = FileLoader.load("events/discover-feature-event.json");
        DiscoverFeatureEvent discoverFeatureEvent = parser.parseValueSave(jsonIn, DiscoverFeatureEvent.class).orElseThrow();
        String jsonOut = GsonConfig.prettyPrinter().toJson(discoverFeatureEvent);
        Assertions.assertEquals(jsonIn, jsonOut);
    }

    @Test
    void testParseProblemReportDescriptionObject() {
        String jsonIn = FileLoader.load("events/problem-report-object.json");
        ProblemReport problemReport = parser.parseValueSave(jsonIn, ProblemReport.class).orElseThrow();
        Assertions.assertEquals("dummy", problemReport.resolveProblemDescription());
        Assertions.assertEquals("message-parse-failure", problemReport.resolveProblemCode());
    }

    @Test
    void testParseProblemReportDescriptionString() {
        String jsonIn = FileLoader.load("events/problem-report-string.json");
        ProblemReport problemReport = parser.parseValueSave(jsonIn, ProblemReport.class).orElseThrow();
        Assertions.assertEquals("dummy", problemReport.resolveProblemDescription());
        Assertions.assertNull(problemReport.resolveProblemCode());
    }

    @Data @NoArgsConstructor
    public static final class Masterdata {
        private String name;
        @AttributeName("local_name")
        private String localName;
        private String street;
        @AttributeName("street_number")
        private String streetNumber;
        private String city;
        private String state;
        @AttributeName("postal_code")
        private String postalCode;
        private String country;
        private String website;
        private String phone;
        private String email;
        @AttributeName("registration_number")
        private String registrationNumber;
        @AttributeName("registration_country")
        private String registrationCountry;
    }

    @Data @NoArgsConstructor
    public static final class BankAccount {
        private String iban;
        private String bic;
    }
}
