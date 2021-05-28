/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import org.hyperledger.aries.IntegrationTestBase;
import org.hyperledger.aries.api.exception.AriesException;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialProposalRequest;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialStoreRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.Assert.*;

class IssueCredentialTest extends IntegrationTestBase {

    @Test
    void testIssueCredential() {
        V1CredentialProposalRequest ic = new V1CredentialProposalRequest();
        assertThrows(AriesException.class, () -> ac.issueCredentialSend(ic));
    }

    @Test
    void testIssueCredentialStore() throws Exception {
        String uuid = UUID.randomUUID().toString();
        try {
            ac.issueCredentialRecordsStore(uuid, V1CredentialStoreRequest.builder().credentialId(uuid).build());
            Assertions.fail("Expected AriesException to be thrown");
        } catch (AriesException e) {
            Assertions.assertTrue(e.getMessage().startsWith("Record not found: " + uuid + "."));
        }
    }

}
