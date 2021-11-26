/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
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
import com.google.gson.annotations.SerializedName;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hyperledger.aries.config.CredDefId;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @see <a href="https://www.w3.org/2018/credentials/v1#VerifiableCredential">VerifiableCredential</a>
 */
@Data @SuperBuilder @NoArgsConstructor
@JsonPropertyOrder({ "@context", "type" })
@JsonInclude(Include.NON_NULL)
public class VerifiableCredential {

    @Builder.Default
    @NonNull
    @SerializedName("@context")
    @JsonProperty("@context")
    private List<Object> context = List.of("https://www.w3.org/2018/credentials/v1");

    @Nullable
    @SerializedName("credentialSubject")
    private Object credentialSubject;

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

    // Verifiable Indy Credential

    @SuperBuilder @NoArgsConstructor
    @Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static class LabeledVerifiableCredential extends VerifiableCredential {
        private String label;
    }

    /**
     * @see <a href="https://raw.githubusercontent.com/iil-network/contexts/master/indycredential.jsonld">VerifiableIndyCredential</a>
     */
    @SuperBuilder @NoArgsConstructor
    @Data @EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
    @JsonInclude(Include.NON_NULL)
    public static class VerifiableIndyCredential extends LabeledVerifiableCredential {
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
}
