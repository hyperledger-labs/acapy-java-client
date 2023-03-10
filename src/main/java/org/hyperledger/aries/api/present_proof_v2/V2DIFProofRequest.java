/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hyperledger.acy_py.generated.model.DIFHolder;
import org.hyperledger.acy_py.generated.model.DIFOptions;
import org.hyperledger.acy_py.generated.model.SubmissionRequirements;
import org.hyperledger.aries.api.jsonld.ProofType;

import java.util.List;
import java.util.UUID;

/**
 * DIFProofRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V2DIFProofRequest {

    private DIFOptions options;
    private PresentationDefinition presentationDefinition;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PresentationDefinition {
        private ClaimFormat format;
        private UUID id;
        private List<InputDescriptors> inputDescriptors;
        private String name;
        private String purpose;
        private List<SubmissionRequirements> submissionRequirements;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class ClaimFormat {

            private Object jwt;
            private Object jwtVc;
            private Object jwtVp;
            private Object ldp;
            private Object ldpVc;
            /** Only ldp-vp format is supported by aca-py */
            private LdpVp ldpVp;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public static class LdpVp {
                @Singular("addProofType")
                List<ProofType> proofType;
            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class InputDescriptors {

            private Constraints constraints;
            private List<String> group;
            private String id;
            private Object metadata;
            private String name;
            private String purpose;
            private List<SchemaInputDescriptorUri> schema;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public static class SchemaInputDescriptorUri {
                private String uri;
            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Constraints {
            private List<DIFField> fields;
            private List<DIFHolder> isHolder;
            /** required means selective disclosure */
            private SubjectEnum limitDisclosure;
            private StatusEnum statusActive;
            private StatusEnum statusRevoked;
            private StatusEnum statusSuspended;
            private SubjectEnum subjectIsIssuer;

            public enum StatusEnum {

                @JsonProperty("required")
                @SerializedName("required")
                REQUIRED,

                @JsonProperty("allowed")
                @SerializedName("allowed")
                ALLOWED,

                @JsonProperty("disallowed")
                @SerializedName("disallowed")
                DISALLOWED
            }

            public enum SubjectEnum {

                @JsonProperty("required")
                @SerializedName("required")
                REQUIRED,

                @JsonProperty("preferred")
                @SerializedName("preferred")
                PREFERRED
            }
        }
    }

    /**
     * When using the /send-presentation endpoint aca-py needs the presentation request,
     * but without the holder.
     */
    public V2DIFProofRequest resetHolderConstraints() {
        if (presentationDefinition != null && presentationDefinition.getInputDescriptors() != null) {
            presentationDefinition.getInputDescriptors().forEach(id -> {
                if (id.getConstraints() != null) {
                    id.getConstraints().setIsHolder(null);
                }
            });
        }
        return this;
    }
}
