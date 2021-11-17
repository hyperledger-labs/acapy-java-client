/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.IntegrationTestBase;
import org.hyperledger.aries.api.exception.AriesException;
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.pojo.PojoProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Slf4j
class PresentProofRequestHelperTest extends IntegrationTestBase {

    @Test
    void testBuildForEachAttributeNoRestrictions() throws Exception {
        PresentProofRequest presentProofRequest = PresentProofRequestHelper
                .buildForEachAttribute("dummy", Set.of("name", "email"), List.of());

        log.debug("{}", GsonConfig.prettyPrinter().toJson(presentProofRequest));

        PresentationExchangeRecord resp = ac.presentProofCreateRequest(presentProofRequest).orElseThrow();

        log.debug("{}", GsonConfig.prettyPrinter().toJson(resp));

        Assertions.assertNotNull(resp.getPresentationExchangeId());
        Assertions.assertEquals(2, resp.getPresentationRequest().getRequestedAttributes().size());
    }

    @Test
    void testBuildForEachAttributeWithRestrictions() throws Exception {
        PresentProofRequest presentProofRequest = PresentProofRequestHelper
                .buildForEachAttribute("dummy", DummyPojo.class, PresentProofRequest.ProofRequest.ProofRestrictions
                        .builder()
                        .issuerDid("did:sov:123")
                        .build());

        log.debug("{}", GsonConfig.prettyPrinter().toJson(presentProofRequest));

        PresentationExchangeRecord resp = ac.presentProofCreateRequest(presentProofRequest).orElseThrow();
        Assertions.assertEquals(presentProofRequest.getProofRequest().getRequestedAttributes(),
                resp.getPresentationRequest().getRequestedAttributes());
        log.debug("{}", GsonConfig.prettyPrinter().toJson(resp));
    }

    @Test
    void testBuildForAllAttributesNoRestrictions() {
        PresentProofRequest presentProofRequest = PresentProofRequestHelper
                .buildForAllAttributes("dummy", "my-mail", Set.of("name", "email"), List.of(), null);
        log.debug("{}", GsonConfig.prettyPrinter().toJson(presentProofRequest));
        Assertions.assertThrows(AriesException.class, () -> ac.presentProofCreateRequest(presentProofRequest));
    }

    @Test
    void testBuildForAllAttributesWithRestrictions() throws Exception {
        PresentProofRequest presentProofRequest = PresentProofRequestHelper
                .buildForAllAttributes("dummy", DummyPojo.class, List.of(PresentProofRequest.ProofRequest.ProofRestrictions
                        .builder()
                        .issuerDid("did:sov:123")
                        .build()));

        log.debug("{}", GsonConfig.prettyPrinter().toJson(presentProofRequest));

        PresentationExchangeRecord resp = ac.presentProofCreateRequest(presentProofRequest).orElseThrow();
        Assertions.assertEquals(presentProofRequest.getProofRequest().getRequestedAttributes(),
                resp.getPresentationRequest().getRequestedAttributes());
        log.debug("{}", GsonConfig.prettyPrinter().toJson(resp));
    }

    @Test
    void testBuildForAllWithTwoAttributeGroups() throws Exception {
        PresentProofRequest.ProofRequest.ProofNonRevoked nonRevoked = PresentProofRequest.ProofRequest.ProofNonRevoked
                .builder()
                .from(Instant.now().minus(Duration.ofMinutes(30)).getEpochSecond())
                .to(Instant.now().getEpochSecond())
                .build();
        PresentProofRequest.ProofRequest.ProofRequestedAttributes corporateId = PresentProofRequestHelper.buildAttributeForAll(
                PojoProcessor.fieldNames(DummyPojo.class),
                List.of(PresentProofRequest.ProofRequest.ProofRestrictions
                        .builder()
                        .issuerDid("did:indy:123")
                        .build()),
                nonRevoked);
        PresentProofRequest.ProofRequest.ProofRequestedAttributes masterId = PresentProofRequestHelper.buildAttributeForAll(
                PojoProcessor.fieldNames(OtherDummyPojo.class),
                List.of(PresentProofRequest.ProofRequest.ProofRestrictions
                        .builder()
                        .schemaIssuerDid("did:indy:432")
                        .build()),
                nonRevoked);
        PresentProofRequest presentProofRequest = PresentProofRequestHelper
                .buildProofRequest(null, Map.of(
                        "dummyPojo", corporateId,
                        "otherDummyPojo", masterId));

        log.debug("{}", GsonConfig.prettyPrinter().toJson(presentProofRequest));

        Optional<PresentationExchangeRecord> resp = ac.presentProofCreateRequest(presentProofRequest);
        Assertions.assertTrue(resp.isPresent());

        log.debug("{}", GsonConfig.prettyPrinter().toJson(resp.get()));
    }

    @Data @NoArgsConstructor
    private static final class DummyPojo {
        private String name;
        private String email;
    }

    @Data @NoArgsConstructor
    private static final class OtherDummyPojo {
        private String value1;
        private String value2;
    }
}
