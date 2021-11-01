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
import org.hyperledger.acy_py.generated.model.DIFProofProposal;
import org.hyperledger.aries.api.present_proof.PresentProofProposal;

/**
 * V20PresProposalByFormat
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresProposalByFormat {
    private DIFProofProposal dif;
    private PresentProofProposal.PresentationPreview indy;
}
