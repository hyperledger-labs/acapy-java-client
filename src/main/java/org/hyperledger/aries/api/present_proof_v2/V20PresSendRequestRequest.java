/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.acy_py.generated.model.DIFProofRequest;
import org.hyperledger.aries.api.present_proof.PresentProofRequest;

/**
 * V20PresSendRequestRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresSendRequestRequest {

    private Boolean autoVerify;
    private String comment;
    private String connectionId;
    private V20PresRequestByFormat presentationRequest;
    private Boolean trace;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class V20PresRequestByFormat {
        private DIFProofRequest dif;
        private PresentProofRequest.ProofRequest indy;
    }
}
