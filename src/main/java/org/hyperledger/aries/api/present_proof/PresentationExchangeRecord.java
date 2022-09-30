/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hyperledger.acy_py.generated.model.AttachDecorator;
import org.hyperledger.acy_py.generated.model.IndyProofReqPredSpec;
import org.hyperledger.aries.api.ExchangeVersion;
import org.hyperledger.aries.api.issue_credential_v1.ThreadId;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.pojo.AttributeName;
import org.hyperledger.aries.webhook.EventParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class PresentationExchangeRecord extends BasePresExRecord {

    private PresentationProposalDict presentationProposalDict;

    private PresentProofRequest.ProofRequest presentationRequest;

    private PresentationRequestDict presentationRequestDict;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject presentation;

    // part of the websocket message
    private List<Identifier> identifiers;

    private transient ExchangeVersion version;

    public boolean hasCredentialDefinitionId(@NonNull String credentialDefinitionId) {
        if (identifiers != null) {
            return identifiers.stream().anyMatch(i -> credentialDefinitionId.equals(i.getCredentialDefinitionId()));
        }
        return false;
    }

    public boolean hasSchemaId(@NonNull String schemaId) {
        if (identifiers != null) {
            return identifiers.stream().anyMatch(i -> schemaId.equals(i.getSchemaId()));
        }
        return false;
    }

    public JsonObject getPresentation() {
        if (presentation == null) {
            return new JsonObject();
        }
        return presentation;
    }

    /**
     * Sets the received presentation attributes on the instance. Only works for matching names.
     * The {@link AttributeName} annotation can be used to exclude fields from processing or to
     * handle different names.
     * The {@link org.hyperledger.aries.pojo.AttributeGroupName} annotation can be used to specify the attribute group
     * name of the presentation in case there is more than one.
     * @param <T> The class type
     * @param type the POJO template to instantiate
     * @return Instance of the POJO with set properties
     */
    public <T> T from(@NonNull Class<T> type) {
        return EventParser.from(presentation.toString(), type);
    }

    /**
     * Finds the attribute names in the proof and extracts their corresponding values.
     * Note this is brute force and simply returns the first match found for each name
     * It's recommended to use {@link #findRevealedAttributes()} or {@link #findRevealedAttributeGroups()}
     * @param names Set of attribute names
     * @return Map containing the attribute names and their corresponding values
     */
    public Map<String, Object> from(@NonNull Set<String> names) {
        return EventParser.from(presentation.toString(), names);
    }

    /**
     * Low level extractor that maps revealed attributes and values to their group
     * <pre>{@code
     * {
     *   "bank-account": {
     *     "revealed_attributes": {
     *       "bic": "1234",
     *       "iban": "4321"
     *     },
     *     "identifier": {
     *       "schema_id": "M6Mbe3qx7vB4wpZF4sBRjt:2:bank_account:1.0",
     *       "cred_def_id": "F6dB7dMVHUQSC64qemnBi7:3:CL:571:mybank",
     *       "timestamp": "1628609220"
     *     }
     *   }
     * }
     * }
     * </pre>
     *
     * @return revealed attributes mapped to their group
     */
    public Map<String, RevealedAttributeGroup> findRevealedAttributeGroups() {
        return RequestedProofParser.collectRevealedGroups(presentation);
    }

    /**
     * Low level extractor that returns a map of all revealed attributes and their values.
     * <pre>{@code
     * {
     *     "iban": "4321",
     *     "bic": "1234"
     * }
     * }
     * </pre>
     * @return revealed attribute to value mapping
     */
    public Map<String, Object> findRevealedAttributes() {
        return EventParser.getValuesByRevealedAttributes(presentation.toString());
    }

    /**
     * Low level extractor that returns a map of all revealed attributes with the all their information.
     * This is useful for all cases where access to the sub proof index is needed to extract the matching identifier
     * from the identifiers list.
     * <pre>{@code
     * "iban": {
     *     "sub_proof_index": 0,
     *     "raw": "4321",
     *     "encoded": "26574491753489267293487534742481789407179815570291479106142274998003667228256"
     * },
     * "bic": {
     *     "sub_proof_index": 0,
     *     "raw": "1234",
     *     "encoded": "95644933709556837616493211320418578774074706673436554047751118609009445904569"
     * }
     * }
     * </pre>
     * @return revealed attribute to value mapping
     */
    public Map<String, RevealedAttribute> findRevealedAttributesFull() {
        return EventParser.getValuesByRevealedAttributesFull(presentation.toString());
    }

    @Override
    @JsonIgnore
    public ExchangeVersion getVersion() {
        return version == null ? ExchangeVersion.V1 : version;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Identifier {
        private String schemaId;
        @SerializedName(value = "cred_def_id", alternate = "credential_definition_id")
        private String credentialDefinitionId;
        private String revRegId;
        private String timestamp;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RevealedAttributeGroup {
        @Singular
        private Map<String, String> revealedAttributes;
        private Identifier identifier;
        private RequestedProofType type;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RevealedAttribute {
        private Integer subProofIndex;
        private String raw;
        private String encoded;
    }

    @AllArgsConstructor @Getter
    public enum RequestedProofType {
        REVEALED_ATTRS("revealed_attrs"),
        REVEALED_ATTR_GROUPS("revealed_attr_groups"),
        SELF_ATTESTED_ATTRS("self_attested_attrs"),
        UNREVEALED_ATTRS("unrevealed_attrs"),
        PREDICATES("predicates")
        ;

        private final String name;
    }

    @Data @NoArgsConstructor
    @JsonPropertyOrder({"id", "type", "comment", "presentationProposal"})
    public static class PresentationProposalDict {

        @JsonProperty("@id")
        @SerializedName("@id")
        private String id;

        @JsonProperty("@type")
        @SerializedName("@type")
        private String type;

        private String comment;

        private IndyPresPreview presentationProposal;

        public Set<String> collectProposalReferents() {
            return presentationProposal != null ? presentationProposal.getAttributes().stream()
                    .map(IndyPresPreview.IndyPresAttrSpec::getReferent)
                    .collect(Collectors.toSet()) : Set.of();
        }

        @Data @NoArgsConstructor
        @JsonPropertyOrder({"type", "attributes", "predicates"})
        public static class IndyPresPreview {

            @JsonProperty("@type")
            @SerializedName("@type")
            private String type;

            private List<IndyPresAttrSpec> attributes = new ArrayList<>();

            private List<IndyPresPredSpec> predicates = new ArrayList<>();

            @Data @NoArgsConstructor
            @JsonPropertyOrder({"credDefId", "mimeType", "name", "referent", "value"})
            public static class IndyPresAttrSpec {

                @JsonProperty("cred_def_id")
                @SerializedName("cred_def_id")
                private String credDefId;

                @JsonProperty("mime-type")
                @SerializedName("mime-type")
                private String mimeType;

                private String name;

                private String referent;

                private String value;
            }

            @Data @NoArgsConstructor
            @JsonPropertyOrder({"credDefId", "name", "predicate", "threshold"})
            public static class IndyPresPredSpec {

                @JsonProperty("cred_def_id")
                @SerializedName("cred_def_id")
                private String credDefId;

                private String name;

                private IndyProofReqPredSpec.PTypeEnum predicate;

                private Integer threshold;
            }
        }
    }

    @Data @NoArgsConstructor
    public static class PresentationRequestDict {
        @JsonProperty("@id")
        @SerializedName("@id")
        private String id;

        @JsonProperty("@type")
        @SerializedName("@type")
        private String type;

        private String comment;

        @JsonProperty("~thread")
        @SerializedName("~thread")
        private ThreadId threadId;

        @JsonProperty("request_presentations~attach")
        @SerializedName("request_presentations~attach")
        private List<AttachDecorator> requestPresentationAttach;
    }
}