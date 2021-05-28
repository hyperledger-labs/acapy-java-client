/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.jsonld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.*;
import org.hyperledger.aries.api.jsonld.VerifiableCredential.VerifiableIndyCredential;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @see <a href="https://www.w3.org/2018/credentials/v1#VerifiablePresentation">VerifiablePresentation</a>
 *
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonPropertyOrder({ "@context", "type" })
public class VerifiablePresentation<T extends VerifiableCredential> {

    public static final transient Type VERIFIABLE_CREDENTIAL_TYPE =
            new TypeToken<VerifiablePresentation<VerifiableCredential>>(){}.getType();

    public static final transient Type INDY_CREDENTIAL_TYPE =
            new TypeToken<VerifiablePresentation<VerifiableIndyCredential>>(){}.getType();

    @Builder.Default
    @NonNull @Nonnull
    @SerializedName("@context")
    @JsonProperty("@context")
    private List<String> context = List.of("https://www.w3.org/2018/credentials/v1");

    @Nullable
    private String id;

    @Builder.Default
    @NonNull @Nonnull
    private List<String> type = List.of("VerifiablePresentation");

    @Nullable
    @SerializedName("verifiableCredential")
    private List<T> verifiableCredential;

    @Nullable
    private Proof proof;

    public List<T> getVerifiableCredential() {
        return verifiableCredential != null ? verifiableCredential : new ArrayList<>();
    }
}
