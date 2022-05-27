/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import org.hyperledger.aries.api.jsonld.VerifiablePresentation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VPPatternMatcherTest {

    @Test
    void testPatterMatcherMatch() {
        VerifiablePresentation.PresentationSubmission.DescriptorMap descriptorMap = VerifiablePresentation
                .PresentationSubmission.DescriptorMap
                .builder()
                .path("$.verifiableCredential[12]")
                .build();
        Assertions.assertEquals(12, descriptorMap.getPathAsIndex());
    }

    @Test
    void testPatterMatcherNoMatch() {
        VerifiablePresentation.PresentationSubmission.DescriptorMap descriptorMap = VerifiablePresentation
                .PresentationSubmission.DescriptorMap
                .builder()
                .path("something")
                .build();
        Assertions.assertNull(descriptorMap.getPathAsIndex());
    }

    @Test
    void testPatterMatcherEmpty() {
        VerifiablePresentation.PresentationSubmission.DescriptorMap descriptorMap = VerifiablePresentation
                .PresentationSubmission.DescriptorMap
                .builder()
                .build();
        Assertions.assertNull(descriptorMap.getPathAsIndex());
    }
}
