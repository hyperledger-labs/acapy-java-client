/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Model for /present-proof/records/{pres_ex_id}/send-presentation
 */
@Data @Builder
public class SendPresentationRequest {

    private Boolean autoRemove;

    private Boolean trace;

    @Builder.Default
    private Map<String, IndyRequestedCredsRequestedAttr> requestedAttributes = new HashMap<>();

    @Builder.Default
    private Map<String, IndyRequestedCredsRequestedPred> requestedPredicates = new HashMap<>();

    @Builder.Default
    private Map<String, String> selfAttestedAttributes = new HashMap<>();

    @Data @Builder
    public static final class IndyRequestedCredsRequestedPred {
        private String credId;
        private Integer timestamp;
    }

    @Data @Builder
    public static final class IndyRequestedCredsRequestedAttr {
        private Boolean revealed;
        private String credId;
    }
}
