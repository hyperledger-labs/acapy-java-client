/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ConnectionStaticRequest {
    private String alias;
    private String myDid;
    private String mySeed;
    private String theirDid;
    private String theirEndpoint;
    private String theirLabel;
    private String theirSeed;
    private String theirVerkey;
}
