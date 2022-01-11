/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.discover_features;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Webhook/Websocket discover feature event message. Contains features the other agent disclosed.
 */
@Data @NoArgsConstructor
public class DiscoverFeatureEvent {
    private String updatedAt;
    private String createdAt;
    private Boolean trace;
    private UUID discoveryExchangeId;
    private UUID connectionId;
    private DisclosedFeatures disclosures;

    @Data @NoArgsConstructor
    public static final class DisclosedFeatures {
        @SerializedName("@type")
        private String type;

        @SerializedName("@id")
        private String id;

        private List<Feature> disclosures;

        @Data @NoArgsConstructor
        public static final class Feature {
            @SerializedName(("feature-type"))
            private String featureType;

            private String id;

            private List<String> roles;
        }
    }
}
