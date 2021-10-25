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
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.pojo.AttributeName;
import org.hyperledger.aries.webhook.EventParser;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PresentationExchangeRecord implements PresExStateTranslator {

    private Boolean autoPresent;
    private String connectionId;
    private String createdAt;
    private String errorMsg;
    private PresentationExchangeInitiator initiator;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject presentation;
    private String presentationExchangeId;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject presentationProposalDict;
    private PresentProofRequest.ProofRequest presentationRequest;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject presentationRequestDict;
    private PresentationExchangeRole role;
    private PresentationExchangeState state;
    private String threadId;
    private Boolean trace;
    private String updatedAt;
    private Boolean verified;

    // part of the websocket message
    private List<Identifier> identifiers;

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
        return EventParser.getValuesByAttributeGroup(presentation.toString());
    }

    /**
     * Low level extractor that returns a map of all revealed attributes and their values
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
    }
}