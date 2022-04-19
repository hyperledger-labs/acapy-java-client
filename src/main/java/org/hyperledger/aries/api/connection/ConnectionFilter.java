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

import javax.annotation.Nullable;

@Data @Builder
public class ConnectionFilter implements AcaPyRequestFilter {
    @Nullable private String alias;
    @Nullable private ConnectionRecord.ConnectionProtocol connectionProtocol;
    @Nullable private String invitationKey;
    @Nullable private String myDid;
    @Nullable private ConnectionState state;
    @Nullable private String theirDid;
    @Nullable private String theirPublicDid;
    @Nullable private ConnectionTheirRole theirRole;
}
