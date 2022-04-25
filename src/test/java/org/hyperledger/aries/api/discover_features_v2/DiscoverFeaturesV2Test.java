/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.discover_features_v2;

import org.hyperledger.acy_py.generated.model.V20DiscoveryRecord;
import org.hyperledger.aries.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DiscoverFeaturesV2Test extends IntegrationTestBase {

    @Test
    void testQueryV2() throws Exception {
        V20DiscoveryRecord v20DiscoveryRecord = ac
                .discoverFeaturesV2Queries(DiscoverFeaturesV2QueriesFilter.builder().build()).orElseThrow();
        Assertions.assertNotNull(v20DiscoveryRecord.getDisclosures());
        Assertions.assertNotNull(v20DiscoveryRecord.getDisclosures().getAtType());
    }
}
