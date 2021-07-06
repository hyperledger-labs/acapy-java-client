/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data @NoArgsConstructor @Accessors(chain = true)
public class ConnectionRecord {

    private ConnectionAcceptance accept;
    private String alias;
    private String connectionId;
    private ConnectionProtocol connectionProtocol;
    private String createdAt;
    private String errorMsg;
    private String inboundConnectionId;
    private String invitationKey;
    private InvitationMode invitationMode;
    private String invitationMsgId;
    private String myDid;
    private String requestId;
    private String rfc23Sate;
    private RoutingState routingState;
    private ConnectionState state;
    private String theirDid;
    private String theirLabel;
    private String theirPublicDid;
    private ConnectionTheirRole theirRole;
    private String updatedAt;

    /**
     * Tries to determine the direction of a connection
     * @return true in case the connection was not initiated by this agent
     */
    public boolean isIncomingConnection() {
        return ConnectionTheirRole.INVITEE.equals(theirRole);
    }

    public boolean isActive() {
        return ConnectionState.ACTIVE.equals(state);
    }

    public enum InvitationMode {
        @JsonProperty("once")
        @SerializedName("once")
        ONCE,

        @JsonProperty("multi")
        @SerializedName("multi")
        MULTI,

        @JsonProperty("static")
        @SerializedName("static")
        STATIC,
    }

    public enum RoutingState {
        @JsonProperty("none")
        @SerializedName("none")
        NONE,

        @JsonProperty("request")
        @SerializedName("request")
        REQUEST,

        @JsonProperty("active")
        @SerializedName("active")
        ACTIVE,

        @JsonProperty("error")
        @SerializedName("error")
        ERROR
    }

    @Getter @AllArgsConstructor
    public enum ConnectionProtocol {

        @JsonProperty("connections/1.0")
        @SerializedName("connections/1.0")
        CONNECTION_V1("connections/1.0"),

        @JsonProperty("didexchange/1.0")
        @SerializedName("didexchange/1.0")
        DID_EXCHANGE_V1("didexchange/1.0")
        ;

        private final String value;
    }
}
