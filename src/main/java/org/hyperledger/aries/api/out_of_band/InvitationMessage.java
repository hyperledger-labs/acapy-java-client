/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.hyperledger.acy_py.generated.model.AttachDecorator;

import java.util.List;

@Data @Builder
public class InvitationMessage {

    @SerializedName("@id")
    private String atId;

    @SerializedName("@type")
    private String atType;

    private List<String> handshakeProtocols;

    private String label;

    @SerializedName("requests~attach")
    private List<AttachDecorator> requestsTildeAttach;

    private List<InvitationMessageService> services;

    @Data @Builder
    public static class InvitationMessageService {

        private String did;

        private String id;

        @SerializedName("recipientKeys")
        private List<String> recipientKeys;

        @SerializedName("routingKeys")
        private List<String> routingKeys;

        @SerializedName("serviceEndpoint")
        private String serviceEndpoint;

        private String type;
    }
}
