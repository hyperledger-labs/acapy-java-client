/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hyperledger.acy_py.generated.model.CredentialStatusOptions;
import org.hyperledger.acy_py.generated.model.V20CredFilterIndy;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.jsonld.VerifiableCredential;

import java.util.ArrayList;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class V2CredentialSendRequest {
    private Boolean autoRemove;
    private String comment;
    private String connectionId;
    private V2CredentialPreview credentialPreview;
    private V20CredFilter filter;
    private Boolean trace;

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class V2CredentialPreview {
        @Builder.Default
        @SerializedName("@type")
        private String type = "issue-credential/2.0/credential-preview";

        private List<CredentialAttributes> attributes = new ArrayList<>();
    }

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class V20CredFilter {
        private V20CredFilterIndy indy;
        private LDProofVCDetail ldProof;
    }

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class LDProofVCDetail {
        private VerifiableCredential credential;
        private LDProofVCDetailOptions options;
    }

    @Data @AllArgsConstructor @NoArgsConstructor @Builder
    public static class LDProofVCDetailOptions {
        private String challenge;
        private String created;
        private CredentialStatusOptions credentialStatus;
        private String domain;
        private String proofPurpose;
        @Builder.Default
        private ProofType proofType = ProofType.BbsBlsSignature2020;
    }

    public enum ProofType {
        Ed25519Signature2018,
        BbsBlsSignature2020
    }
}
