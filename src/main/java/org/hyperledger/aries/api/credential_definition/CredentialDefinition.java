/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credential_definition;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.CredDefId;

import java.util.List;

/**
 * Schemas are the same for all clients once they are agreed upon.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public final class CredentialDefinition {

    private String ver;

    private String id;

    @SerializedName("schemaId")
    private String schemaId;

    private String type;

    private String tag;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject value;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static final class CredentialDefinitionRequest {
        private Integer revocationRegistrySize;
        private String schemaId;
        private Boolean supportRevocation;
        private String tag;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static final class CredentialDefinitionResponse {
        @SerializedName(value = CredDefId.CRED_DEF_ID, alternate = CredDefId.CREDENTIAL_DEFINITION_ID)
        private String credentialDefinitionId;
    }

    @Data @NoArgsConstructor
    public static final class CredentialDefinitionsCreated {
        private List<String> credentialDefinitionIds;
    }
}
