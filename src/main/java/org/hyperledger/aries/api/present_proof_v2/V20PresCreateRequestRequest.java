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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class V20PresCreateRequestRequest {
    private Boolean autoRemove;
    private Boolean autoVerify;
    private String comment;
    private V20PresSendRequestRequest.V20PresRequestByFormat presentationRequest;
    private Boolean trace;
}
