/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Model for:
 * @see <a href="https://aries-cloud-agent-python.readthedocs.io/en/latest/generated/aries_cloudagent.protocols.present_proof.v1_0/?highlight=IndyCredPrecisSchema#aries_cloudagent.protocols.present_proof.v1_0.routes.IndyCredPrecisSchema">IndyCredPrecisSchema</a>
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class PresentationRequestCredentials {

    @SerializedName(value = "cred_info", alternate = "credential_info")
    private CredentialInfo credentialInfo;

    private Interval interval;

    private List<String> presentationReferents;

    @Data @NoArgsConstructor
    public static final class CredentialInfo {

        private String referent;

        private Map<String, String> attrs;

        private String schemaId;

        @SerializedName(value = "cred_def_id", alternate = "credential_definition_id")
        private String credentialDefinitionId;

        private String revRegId;

        @SerializedName(value = "cred_rev_id", alternate = "credential_revision_id")
        private String credentialRevisionId;

    }

    /**
     * Non-revocation interval from presentation request
     */
    @Data @NoArgsConstructor
    public static final class Interval {
        private Integer to;
    }
}
