/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.resolver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.acy_py.generated.model.DIDEndpointWithType;

import java.util.List;
import java.util.Optional;

/**
 * W3C Did Document
 * https://www.w3.org/TR/did-core/#core-properties
 */
@JsonPropertyOrder({"context", "id"})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DIDDocument {

    @JsonProperty("@context")
    @SerializedName("@context")
    private String context = "https://www.w3.org/ns/did/v1";

    private String id;

    @SerializedName("alsoKnownAs")
    private List<String> alsoKnownAs;

    private String controller;

    @SerializedName("verificationMethod")
    private List<VerificationMethod> verificationMethod;

    // structures below have the following content:
    // String did = indexAt[0], VerificationMethod method = indexAt[1]
    private List<Object> authentication;
    @SerializedName("assertionMethod")
    private List<Object> assertionMethod;
    @SerializedName("keyAgreement")
    private List<Object> keyAgreement;
    @SerializedName("capabilityInvocation")
    private List<Object> capabilityInvocation;
    @SerializedName("capabilityDelegation")
    private List<Object> capabilityDelegation;

    private List<Service> service;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class VerificationMethod {
        private String id;
        private String type;
        private String controller;
        @SerializedName("publicKeyBase58")
        private String publicKeyBase58;
    }

    /**
     * @see <a href=
     *      "https://www.w3.org/TR/did-core/#service-endpoints">did-core/#service-endpoints</a>
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Service {
        private String id;
        private String type;
        @SerializedName("serviceEndpoint")
        private String serviceEndpoint;
        @SerializedName("recipientKeys")
        private List<String> recipientKeys;
        @SerializedName("routingKeys")
        private List<String> routingKeys;
        private Integer priority;
    }

    public List<Service> getService() {
        if (service == null) {
            return List.of();
        }
        return service;
    }

    public Optional<String> findPublicProfileUrl() {
        return findUrlByType(DIDEndpointWithType.EndpointTypeEnum.PROFILE);
    }

    public Optional<String> findAriesEndpointUrl() {
        return findUrlByType(DIDEndpointWithType.EndpointTypeEnum.ENDPOINT);
    }

    private Optional<String> findUrlByType(@NonNull DIDEndpointWithType.EndpointTypeEnum type) {
        return getService()
                .stream()
                .filter(s -> StringUtils.equalsIgnoreCase(type.getValue(), s.getType()))
                .findFirst()
                .map(Service::getServiceEndpoint);
    }

    public boolean hasProfileEndpoint() {
        return getService().stream()
                .anyMatch(s -> StringUtils.equalsIgnoreCase(
                        DIDEndpointWithType.EndpointTypeEnum.PROFILE.getValue(), s.getType()));
    }

    public boolean hasAriesEndpoint() {
        return getService().stream()
                .anyMatch(s ->
                        StringUtils.equalsIgnoreCase(
                                DIDEndpointWithType.EndpointTypeEnum.ENDPOINT.getValue(), s.getType())
                        || StringUtils.equalsIgnoreCase("did-communication", s.getType()));
    }
}
