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
import org.hyperledger.aries.api.present_proof.SendPresentationRequest;

/**
 * V20PresSpecByFormatRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresSpecByFormatRequest {
    private Boolean autoRemove;
    private DIFPresSpec dif;
    private SendPresentationRequest indy;
    private Boolean trace;
}
