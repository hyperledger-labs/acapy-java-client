/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @see <a href="https://www.w3.org/2018/credentials/v1#VerifiablePresentation">VerifiablePresentation</a>
 *
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonPropertyOrder({ "@context", "type" })
public final class VerifiablePresentation<T extends VerifiableCredential> {

    public static final Type VERIFIABLE_CREDENTIAL_TYPE =
            new TypeToken<VerifiablePresentation<VerifiableCredential>>(){}.getType();

    public static final Type INDY_CREDENTIAL_TYPE =
            new TypeToken<VerifiablePresentation<VerifiableIndyCredential>>(){}.getType();

    @Builder.Default
    @NonNull @Nonnull
    @SerializedName("@context")
    @JsonProperty("@context")
    private List<String> context = List.of("https://www.w3.org/2018/credentials/v1");

    @Builder.Default
    @NonNull @Nonnull
    private List<String> type = List.of("VerifiablePresentation");

    @Nullable
    private String id;

    @Nullable
    @SerializedName("verifiableCredential")
    private List<T> verifiableCredential;

    private PresentationSubmission presentationSubmission;

    @Nullable
    private LinkedDataProof proof;

    public List<T> getVerifiableCredential() {
        return verifiableCredential != null ? verifiableCredential : new ArrayList<>();
    }

    /**
     * aca-py specific content
     */
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static final class PresentationSubmission {
        private UUID id;
        private UUID definitionId;
        private List<DescriptorMap> descriptorMap = List.of();

        @Data @Builder @NoArgsConstructor @AllArgsConstructor
        public static final class DescriptorMap {

            private static final Pattern PATH_PATTERN = Pattern.compile("^.*?\\[\\D*(\\d+)\\D*].*$");

            private String id;
            private String format;
            private String path;

            /**
             * Assumes that the path is only one level deep e.g. '$.verifiableCredential[0]'
             * @return path index as {@link Integer} or null
             */
            public Integer getPathAsIndex() {
                if (path == null) {
                    return null;
                }
                Matcher matcher = PATH_PATTERN.matcher(path);
                return matcher.find() ? Integer.parseInt(matcher.group(1)) : null;
            }
        }
    }
}
