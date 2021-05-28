/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @Builder
public class ConnectionAcceptInvitationFilter implements AcaPyRequestFilter {
    /** Identifier for active mediation record to be used*/
    private String mediationId;

    /** My URL endpoint*/
    private String myEndpoint;

    /** Label for connection*/
    private String myLabel;
}
