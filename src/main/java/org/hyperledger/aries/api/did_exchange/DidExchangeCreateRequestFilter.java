/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
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
public final class DidExchangeCreateRequestFilter implements AcaPyRequestFilter {
    /** Public DID to which to request connection */
    private String theirPublicDid;

    /** Alias for connection */
    private String alias;

    /** Identifier for active mediation record to be used */
    private String mediationId;

    /** My URL endpoint */
    private String myEndpoint;

    /** Label for connection request */
    private String myLabel;

    /** Use public did for this connection */
    private Boolean usePublicDid;

    @Builder
    public DidExchangeCreateRequestFilter(@NonNull String theirPublicDid, String alias, String mediationId,
        String myEndpoint, String myLabel, Boolean usePublicDid) {
        this.theirPublicDid = theirPublicDid;
        this.alias = alias;
        this.mediationId = mediationId;
        this.myEndpoint = myEndpoint;
        this.myLabel = myLabel;
        this.usePublicDid = usePublicDid;
    }
}
