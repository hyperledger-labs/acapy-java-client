/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.discover_features;

import org.hyperledger.acy_py.generated.model.V10DiscoveryRecord;
import org.hyperledger.aries.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DiscoverFeaturesTest extends IntegrationTestBase {

    @Test
    void testQuery() throws Exception {
        V10DiscoveryRecord v10DiscoveryRecord = ac.discoverFeaturesQuery(DiscoverFeaturesQueryFilter.builder().build())
                .orElseThrow();
        Assertions.assertNotNull(v10DiscoveryRecord.getDisclose());
        Assertions.assertNotNull(v10DiscoveryRecord.getDisclose().getAtId());
    }
}
