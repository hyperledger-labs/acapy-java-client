/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import lombok.*;
import org.hyperledger.acy_py.generated.model.DIFOptions;
import org.hyperledger.aries.api.present_proof.PresentProofRequest;

import java.util.List;

/**
 * V20PresProposalByFormat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresProposalByFormat {
    private DIFProofProposal dif;
    private PresentProofRequest.ProofRequest indy;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DIFProofProposal {
        @Singular
        private List<V2DIFProofRequest.PresentationDefinition.InputDescriptors> inputDescriptors;
        private DIFOptions options;
    }
}
