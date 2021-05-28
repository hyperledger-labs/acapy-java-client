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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.present_proof.PresentProofProposal.PresentationPreview.PresAttrSpec;
import org.hyperledger.aries.config.CredDefId;

import java.util.List;

/**
 * Aka PresentationProposalRequest
 * This model is used to build a presentation proposal request, or in other words to send a proof.
 *
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PresentProofProposal {

    private String connectionId;

    private PresentationPreview presentationProposal;

    private Boolean trace;

    private Boolean autoPresent;

    private String comment;

    public PresentProofProposal(String connectionId, List<PresAttrSpec> attr) {
        super();
        this.connectionId = connectionId;
        this.presentationProposal = new PresentationPreview(attr);
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static final class PresentationPreview {
        @SerializedName("@type") @Builder.Default
        private String type = "did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/present-proof/1.0/presentation-preview";
        @Builder.Default
        private List<PresAttrSpec> attributes = List.of();
        @Builder.Default
        private List<PresPredSpec> predicates = List.of();

        public PresentationPreview(List<PresAttrSpec> attributes) {
            super();
            this.attributes = attributes;
        }

        public PresentationPreview(List<PresAttrSpec> attributes, List<PresPredSpec> predicates) {
            this(attributes);
            this.predicates = predicates;
        }

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        public static final class PresAttrSpec {
            private String name;
            private String value;
            @SerializedName(value = CredDefId.CRED_DEF_ID, alternate = CredDefId.CREDENTIAL_DEFINITION_ID)
            private String credentialDefinitionId;
            @SerializedName(value = "mime-type")
            private String mimeType;
            private String referent;
        }

        @Data @NoArgsConstructor @AllArgsConstructor @Builder
        public static final class PresPredSpec {
            private String name;
            @SerializedName(value = CredDefId.CRED_DEF_ID, alternate = CredDefId.CREDENTIAL_DEFINITION_ID)
            private String credentialDefinitionId;
            private String predicate;
            private Integer treshold;
        }
    }


}
