/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.did_exchange;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data
public class DidExchangeAcceptInvitationFilter implements AcaPyRequestFilter {
    private String connId;
    private String myEndpoint;
    private String myLabel;

    @Builder
    public DidExchangeAcceptInvitationFilter(@NonNull String connectionId, String myEndpoint, String myLabel) {
        this.connId = connectionId;
        this.myEndpoint = myEndpoint;
        this.myLabel = myLabel;
    }
}
