/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
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
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.api.issue_credential_v1.CredentialExchangeRole;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRole;
import org.hyperledger.aries.pojo.AttributeName;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EventParserTest {

    private final FileLoader loader = FileLoader.newLoader();
    private final EventParser parser = new EventParser();

    @Test
    void testParseConnectionEvent() {
        String json = loader.load("events/connection-active.json");
        Optional<ConnectionRecord> conn = parser.parseValueSave(json, ConnectionRecord.class);
        Assertions.assertTrue(conn.isPresent());
        Assertions.assertEquals(ConnectionState.ACTIVE, conn.get().getState());
    }

    @Test
    void testParseIssuedCredential() {
        String json = loader.load("events/issue-credential.json");
        Optional<V1CredentialExchange> con = parser.parseValueSave(json, V1CredentialExchange.class);
        Assertions.assertTrue(con.isPresent());
        V1CredentialExchange cred = con.get();
        Assertions.assertEquals(CredentialExchangeRole.HOLDER, cred.getRole());
        Assertions.assertNotNull(cred.getCredentialDefinitionId());
        Assertions.assertNotNull(cred.getCredential());
    }

    @Test
    void testParseProofPresentationVerifier() {
        String json = loader.load("events/proof-valid-verifier.json");
        Optional<PresentationExchangeRecord> p = parser.parsePresentProof(json);
        Assertions.assertTrue(p.isPresent());
        Assertions.assertEquals(PresentationExchangeRole.VERIFIER, p.get().getRole());
        Masterdata md = p.get().from(Masterdata.class);
        Assertions.assertEquals("4", md.getStreetNumber());
        Assertions.assertEquals("8000", md.getPostalCode());
        Assertions.assertNotNull(p.get().getIdentifiers());
        Assertions.assertEquals(1, p.get().getIdentifiers().size());
        Assertions.assertTrue(p.get().getIdentifiers().get(0).getSchemaId().startsWith("CHysca6fY8n8ytCDLAJGZj"));
        Assertions.assertTrue(p.get().getIdentifiers().get(0).getCredentialDefinitionId().startsWith("CHysca6fY8n8ytCDLAJGZj"));
    }

    @Test
    void testParseProofPresentationVerifierMap() {
        String json = loader.load("events/proof-valid-verifier.json");
        Optional<PresentationExchangeRecord> p = parser.parsePresentProof(json);
        Assertions.assertTrue(p.isPresent());
        Map<String, Object> md = p.get().from(Set.of("country", "city"));
        Assertions.assertEquals("Switzerland", md.get("country"));
        Assertions.assertEquals("Zürich", md.get("city"));
    }

    @Test
    void testParseProofPresentationProver() {
        String json = loader.load("events/proof-valid-prover.json");
        Optional<PresentationExchangeRecord> p = parser.parsePresentProof(json);
        Assertions.assertTrue(p.isPresent());
        Assertions.assertEquals(PresentationExchangeRole.PROVER, p.get().getRole());
        final BankAccount ba = p.get().from(BankAccount.class);
        Assertions.assertNotNull(ba);
        Assertions.assertEquals("GB33BUKB20201555555555", ba.getIban());
        Assertions.assertEquals("PBNK", ba.getBic());
    }

    @Test
    void testParseProofPresentationProverMap() {
        String json = loader.load("events/proof-valid-prover.json");
        Optional<PresentationExchangeRecord> p = parser.parsePresentProof(json);
        Assertions.assertTrue(p.isPresent());
        final Map<String, Object> ba = p.get().from(Set.of("iban", "bic"));
        Assertions.assertNotNull(ba);
        Assertions.assertEquals("GB33BUKB20201555555555", ba.get("iban"));
        Assertions.assertEquals("PBNK", ba.get("bic"));
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
