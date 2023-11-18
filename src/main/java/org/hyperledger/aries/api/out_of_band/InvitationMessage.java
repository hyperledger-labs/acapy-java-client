/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.hyperledger.acy_py.generated.model.AttachDecorator;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Out Of Band Invitation Message
 * <pre>{@code
 * InvitationMessage<String> inviteString = gson.fromJson(inviteJson, InvitationMessage.STRING_TYPE);
 * InvitationMessage<InvitationMessage.InvitationMessageService> complexType = gson.fromJson(inviteJson, InvitationMessage.RFC0067_TYPE);
 * }
 * </pre>
 * @param <T> tape of the service object either {@link String} or {@link InvitationMessageService}
 */
@Data @Builder
public class InvitationMessage<T> {

    /** Used to deserialize RFC0067 service types */
    public static final Type RFC0067_TYPE = new TypeToken<InvitationMessage<InvitationMessageService>>(){}.getType();

    /** Used to deserialize DID string service types */
    public static final Type STRING_TYPE = new TypeToken<InvitationMessage<String>>(){}.getType();

    @SerializedName("@id")
    private String atId;

    @SerializedName("@type")
    private String atType;

    private String label;

    private String imageUrl;

    @SerializedName("goal_code")
    private String goalCode;

    private String goal;

    /**
     * List of mime type in order of preference
     * example: List [ "didcomm/aip1", "didcomm/aip2;env=rfc19" ]
     */
    private List<String> accept;

    @Builder.Default
    @SerializedName("handshake_protocols")
    private List<String> handshakeProtocols = List.of("did:sov:BzCbsNYhMrjHiqZDTUASHg;spec/didexchange/1.0");

    @SerializedName("requests~attach")
    private List<AttachDecorator> requestsAttach;

    /** Either a DIDComm service object (as per RFC0067) or a DID string. */
    @Singular
    private List<T> services;

    /**
     * DIDComm service object (as per RFC0067)
     */
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

        private List<String> accept;

        private String type;
    }
}
