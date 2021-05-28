/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class InvitationRecord {
    private String createdAt;
    private String inviMsgId;
    private Object invitation;
    private String invitationId;
    private String invitationUrl;
    private String state;
    private Boolean trace;
    private String updatedAt;
}
