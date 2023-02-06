/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hyperledger.aries.api.jsonld.VerifiableCredential;
import org.hyperledger.aries.config.GsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class V20CredExRecordTest {

    @Test
    void testCustomCredentialType() {
        JsonObject issued = GsonConfig.defaultConfig().fromJson(cred, JsonObject.class);
        V20CredExRecord exchange = V20CredExRecord.builder()
                .byFormat(V20CredExRecordByFormat.builder()
                        .credIssue(issued)
                        .build())
                .build();
        CustomCredentialType customCredentialType = exchange.resolveLDCredentialFromCustomType(CustomCredentialType.class);
        Assertions.assertEquals("RSA_RAW", customCredentialType.getProvenanceProof().get(0).getAlgorithm());
    }

    @Data @ToString(callSuper = true) @EqualsAndHashCode(callSuper = true)
    private static final class CustomCredentialType extends VerifiableCredential {
        @SerializedName("provenanceProof")
        private List<ProvenanceProof> provenanceProof;

        @Data
        private static final class ProvenanceProof {
            private String algorithm;
            private List<String> value;
        }
    }
    private final String cred = "{\n" +
            "    \"ld_proof\": {\n" +
            "        \"@context\": [\n" +
            "            \"https://www.w3.org/2018/credentials/v1\",\n" +
            "            \"https://carla.dih-cloud.com/chained-credential-v1.jsonld\",\n" +
            "            \"https://carla.dih-cloud.com/participant-credential-v1.jsonld\"\n" +
            "        ],\n" +
            "        \"credentialSubject\": {\n" +
            "            \"participantDetails\": {\n" +
            "                \"name\": \"string\"\n" +
            "            },\n" +
            "            \"identityDetails\": {\n" +
            "                \"did\": \"did:sov:ArqouCjqi4RwBXQqjAbQrG\",\n" +
            "                \"uniqueIds\": [\n" +
            "                    {\n" +
            "                        \"type\": \"TEST\",\n" +
            "                        \"value\": \"100\"\n" +
            "                    }\n" +
            "                ]\n" +
            "            },\n" +
            "            \"id\": \"did:sov:ArqouCjqi4RwBXQqjAbQrG\"\n" +
            "        },\n" +
            "        \"provenanceProof\": [\n" +
            "            {\n" +
            "                \"algorithm\": \"RSA_RAW\",\n" +
            "                \"value\": [\n" +
            "                    \"bbbbnmWPldib7jnG2k1TN0fTCtX........gvvLs=\"\n" +
            "                ]\n" +
            "            }\n" +
            "        ],\n" +
            "        \"id\": \"urn:uuid:23711187-dec1-4bf6-2227-55710f977333\",\n" +
            "        \"issuanceDate\": \"2019-12-03T12:19:52Z\",\n" +
            "        \"issuer\": \"did:sov:7rB93fLvW5kgujZ4E57ZxL\",\n" +
            "        \"type\": [\n" +
            "            \"VerifiableCredential\",\n" +
            "            \"ParticipantCredential\",\n" +
            "            \"ChainedCredential\"\n" +
            "        ]\n" +
            "    }\n" +
            "}";
}
