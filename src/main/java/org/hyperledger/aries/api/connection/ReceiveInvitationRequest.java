/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import javax.annotation.Nullable;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReceiveInvitationRequest {
    @Nullable
    @SerializedName("@id")
    private String id;

    @SerializedName("@type")
    private String type;

    @Nullable
    private String did;

    @Nullable
    @SerializedName("imageUrl")
    private String imageUrl;

    @Nullable
    private String label;

    @Nullable
    @SerializedName("recipientKeys")
    private List<String> recipientKeys;

    @Nullable
    @SerializedName("routingKeys")
    private List<String> routingKeys;

    @Nullable
    @SerializedName("serviceEndpoint")
    private String serviceEndpoint;

}
