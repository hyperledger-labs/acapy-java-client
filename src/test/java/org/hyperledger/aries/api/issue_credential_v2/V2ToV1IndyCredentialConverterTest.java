/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import org.hyperledger.aries.MockedTestBase;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.util.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class V2ToV1IndyCredentialConverterTest extends MockedTestBase {

    @Test
    void testSimpleV2ToV1() {
        String json = FileLoader.load("files/issue-credential-v2/holder-icv2-done.json");
        V20CredExRecord v20CredExRecord = gson.fromJson(json, V20CredExRecord.class);
        V2ToV1IndyCredentialConverter c = V2ToV1IndyCredentialConverter.INSTANCE();
        Optional<Credential> credential = c.toV1Credential(v20CredExRecord);
        Assertions.assertTrue(credential.isPresent());
        Assertions.assertNotNull(credential.get().getAttrs());
        Assertions.assertEquals(2, credential.get().getAttrs().size());
        Assertions.assertEquals("222", credential.get().getAttrs().get("iban"));
        Assertions.assertEquals("1111", credential.get().getAttrs().get("bic"));
    }
}
