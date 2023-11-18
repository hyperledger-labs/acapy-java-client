/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.jsonld;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hyperledger.aries.api.serializer.JsonObjectDeserializer;
import org.hyperledger.aries.api.serializer.JsonObjectSerializer;
import org.hyperledger.aries.config.CredDefId;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @see <a href="https://www.w3.org/2018/credentials/v1#VerifiableCredential">VerifiableCredential</a>
 */
@Data @SuperBuilder @NoArgsConstructor @AllArgsConstructor
@JsonPropertyOrder({ "@context", "type" })
@JsonInclude(Include.NON_NULL)
public class VerifiableCredential {

    @Builder.Default
    @NonNull
    @SerializedName("@context")
    @JsonProperty("@context")
    private List<Object> context = List.of("https://www.w3.org/2018/credentials/v1");

    @SerializedName("credentialSubject")
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject credentialSubject;

    @Nullable
    @SerializedName("expirationDate")
    private String expirationDate;

    @Nullable
    private String id;

    @Nullable
    @SerializedName("issuanceDate")
    private String issuanceDate;

    @Nullable
    private String issuer;

    @Nullable
    private LinkedDataProof proof;

    @Builder.Default
    @NonNull
    private List<String> type = List.of("VerifiableCredential");

    public Map<String, String> subjectToFlatMap() {
        return credentialSubject == null ? Map.of() : credentialSubject.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getAsString()));
    }

    // Verifiable Indy Credential

    @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    @Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static class LabeledVerifiableCredential extends VerifiableCredential {
        private String label;
    }

    /**
     * @see <a href="https://raw.githubusercontent.com/iil-network/contexts/master/indycredential.jsonld">VerifiableIndyCredential</a>
     */
    @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    @Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static final class VerifiableIndyCredential extends LabeledVerifiableCredential {
        @Nullable
        @SerializedName("indyIssuer")
        private String indyIssuer;

        @Nullable
        @SerializedName("schemaId")
        private String schemaId;

        @Nullable
        @SerializedName(CredDefId.CREDDEFID)
        private String credDefId;
    }

    @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    @Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static final class VerifiableCredentialMatch extends VerifiableCredential {
        private String recordId;
    }
}
