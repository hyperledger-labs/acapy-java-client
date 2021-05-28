/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.IntegrationTestBase;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.exception.AriesException;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class PresentProofProposalTest extends IntegrationTestBase {

    @Test
    void testBuildPresentationProposal() {
        Credential cred = new Credential();
        cred.setAttrs(Map.of("street", "teststreet"));
        cred.setCredentialDefinitionId("WgWxqztrNooG92RXvxSTWv:3:CL:20:tag");
        cred.setReferent("referent");

        final PresentProofProposal proposal = PresentProofProposalBuilder
                .fromCredential("d0fb05d0-b7bb-4b08-9c82-1199133458c4", cred);
        final String json = GsonConfig.prettyPrinter().toJson(proposal);
        log.debug(json);

        // no credential and no connection, but expecting no parsing errors to happen here.
        AriesException e = assertThrows(AriesException.class, () -> ac.presentProofSendProposal(proposal));
        assertTrue(e.getMessage().startsWith("Record not found: d0fb05d0-b7bb-4b08-9c82-1199133458c4."));
    }

    @Test
    void testPresentProofRecordsVerifyPresentation() {
        AriesException e = Assertions.assertThrows(AriesException.class, () ->
                ac.presentProofRecordsVerifyPresentation(UUID.randomUUID().toString()));
        assertTrue(e.getMessage().startsWith("Record not found:"));
    }
}
