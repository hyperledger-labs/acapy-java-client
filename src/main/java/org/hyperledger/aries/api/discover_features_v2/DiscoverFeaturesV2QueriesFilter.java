/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.discover_features_v2;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @Builder
public class DiscoverFeaturesV2QueriesFilter implements AcaPyRequestFilter {
    private String connectionId;
    private String queryGoalCode;
    private String queryProtocol;
}
