/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import com.google.gson.JsonElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class InvitationRecord implements InvitationMessageTranslator {
    private String createdAt;
    private String updatedAt;
    private String inviMsgId;

    /**
     * Invitation payload
     * see {@link InvitationMessage}
     * use {@link InvitationMessageTranslator#asRFC0067Type()} or
     * {@link InvitationMessageTranslator#asStringType()}
     * to resolve the respective payload
     */
    private JsonElement invitation;

    private String invitationId;
    private String invitationUrl;
    private String oobId;
    private OOBRecord.OOBState state;
    private Boolean trace;
}
