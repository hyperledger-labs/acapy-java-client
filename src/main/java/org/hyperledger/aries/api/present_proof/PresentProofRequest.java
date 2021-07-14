/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hyperledger.acy_py.generated.model.IndyProofReqPredSpec;
import org.hyperledger.aries.api.serializer.JsonObjectArrayDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectArraySerializer;
import org.hyperledger.aries.config.CredDefId;
import org.hyperledger.aries.config.GsonConfig;

import java.util.List;
import java.util.Map;

/**
 * Aka PresentationSendRequestRequest
 * This model is used to send a presentation request, or in other words to request a proof.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PresentProofRequest {

    private String connectionId;

    @NonNull
    private ProofRequest proofRequest;

    private Boolean trace;

    private String comment;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProofRequest {

        @Builder.Default
        private String name = "Proof request";

        @Builder.Default
        private String version = "1.0";

        private String nonce;

        private ProofNonRevoked nonRevoked;

        @Singular
        private Map<String, ProofRequestedAttributes> requestedAttributes;

        @Singular
        private Map<String, ProofRequestedPredicates> requestedPredicates;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ProofRequestedAttributes {
            private String name;
            /**
             * @since 0.5.4
             */
            private List<String> names;
            private ProofNonRevoked nonRevoked;
            @Singular
            @JsonSerialize(using = JsonObjectArraySerializer.class)
            @JsonDeserialize(using = JsonObjectArrayDeserializer.class)
            private List<JsonObject> restrictions;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ProofRequestedPredicates {
            private String name;
            private ProofNonRevoked nonRevoked;
            private IndyProofReqPredSpec.PTypeEnum pType;
            private Integer pValue;
            @Singular
            @JsonSerialize(using = JsonObjectArraySerializer.class)
            @JsonDeserialize(using = JsonObjectArrayDeserializer.class)
            private List<JsonObject> restrictions;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ProofNonRevoked {
            private Long from;
            private Long to;
        }

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ProofRestrictions {
            private String schemaId;

            private String schemaName;

            private String schemaVersion;

            private String schemaIssuerDid;

            @SerializedName(value = CredDefId.CRED_DEF_ID, alternate = CredDefId.CREDENTIAL_DEFINITION_ID)
            private String credentialDefinitionId;

            private String issuerDid;

            @Singular
            private transient Map<String, String> genericRestrictions;

            public JsonObject toJsonObject() {
                JsonObject result = GsonConfig.defaultConfig().toJsonTree(this)
                        .getAsJsonObject();
                return flattenGenericRestrictions(result);
            }

            @NonNull
            private JsonObject flattenGenericRestrictions(JsonObject result) {
                genericRestrictions.forEach((k,v)-> result.add(k,new JsonPrimitive(v)));
                return result;
            }

            public static JsonObject addNameValue(
                    @NonNull String name,
                    @NonNull String value,
                    @NonNull JsonObject restriction) {
                restriction.addProperty("attr::" + name + "::value", value);
                return restriction;
            }

            // This extends the lombok generated builder with contained methods.
            public static class ProofRestrictionsBuilder{
                public ProofRestrictionsBuilder addAttributeValueRestriction(@NonNull String name, @NonNull String value) {
                    this.genericRestriction("attr::" + name + "::value", value);
                    return this;
                }
            }
        }
    }
}
