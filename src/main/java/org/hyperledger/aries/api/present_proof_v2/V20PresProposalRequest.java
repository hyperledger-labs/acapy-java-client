/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
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

/**
 * V20PresProposalRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresProposalRequest {
    private Boolean autoPresent;
    private Boolean autoRemove;
    private String comment;
    private String connectionId;
    private V20PresProposalByFormat presentationProposal;
    private Boolean trace;
}
