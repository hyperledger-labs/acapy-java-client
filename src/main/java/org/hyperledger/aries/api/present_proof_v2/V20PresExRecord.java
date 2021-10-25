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
import org.hyperledger.acy_py.generated.model.V20Pres;
import org.hyperledger.acy_py.generated.model.V20PresExRecordByFormat;
import org.hyperledger.acy_py.generated.model.V20PresProposal;
import org.hyperledger.acy_py.generated.model.V20PresRequest;
import org.hyperledger.aries.api.present_proof.PresExStateTranslator;
import org.hyperledger.aries.api.present_proof.PresentationExchangeInitiator;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRole;
import org.hyperledger.aries.api.present_proof.PresentationExchangeState;

/**
 * V20PresExRecord
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresExRecord implements PresExStateTranslator {
    private Boolean autoPresent;
    private String createdAt;
    private String updatedAt;
    private Boolean trace;
    private String errorMsg;

    private Boolean verified;

    private String connectionId;
    private String presExId;
    private String threadId;

    private PresentationExchangeInitiator initiator;
    private PresentationExchangeRole role;
    private PresentationExchangeState state;

    private V20Pres pres;
    private V20PresExRecordByFormat byFormat;
    private V20PresProposal presProposal;
    private V20PresRequest presRequest;
}
