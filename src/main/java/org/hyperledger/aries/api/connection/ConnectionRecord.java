/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data @NoArgsConstructor @Accessors(chain = true)
public class ConnectionRecord {

    private ConnectionAcceptance accept;
    private String alias;
    private String connectionId;
    private String createdAt;
    private String errorMsg;
    private String inboundConnectionId;
    @Deprecated private String initiator;
    private String invitationKey;
    private String invitationMode;
    private String invitationMsgId;
    private String myDid;
    private String requestId;
    private String rfc23Sate;
    private String routingState;
    private ConnectionState state;
    private String theirDid;
    private String theirLabel;
    private String theirPublicDid;
    private ConnectionTheirRole theirRole;
    private String updatedAt;

    /**
     * The request id is only set if the connection was initated by this agent.
     * @return true in case the connection was not initiated by this agent
     */
    public boolean isIncomingConnection() {
        return StringUtils.isEmpty(requestId);
    }

    public boolean isActive() {
        return ConnectionState.ACTIVE.equals(state);
    }
}
