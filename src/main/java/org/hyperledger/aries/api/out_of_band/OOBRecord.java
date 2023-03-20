/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.present_proof.ProofRequestPresentation;

import java.util.UUID;

/**
 * aca-py stored OOB record, used in the webhook/websocket event.
 */
@Data
@NoArgsConstructor
public class OOBRecord implements InvitationMessageTranslator {

    private String createdAt;
    private String updatedAt;

    private OOBState state;
    private UUID inviMsgId;
    private OOBRole role;

    /**
     * Invitation payload
     * see {@link InvitationMessage}
     * use {@link InvitationMessageTranslator#asRFC0067Type()} or
     * {@link InvitationMessageTranslator#asStringType()}
     * to resolve the respective payload
     */
    private JsonElement invitation;

    private ProofRequestPresentation.ServiceDecorator theirService;
    private UUID connectionId;
    private UUID reuseMsgId;
    private UUID oobId;
    private UUID attachThreadId;
    private String ourRecipientKey;
    private ProofRequestPresentation.ServiceDecorator ourService;
    private Boolean multiUse;
    private Boolean trace;

    public enum OOBState {
        @SerializedName("initial")
        INITIAL,
        @SerializedName(value = "prepare-response", alternate = "prepare_response")
        PREPARE_RESPONSE,
        @SerializedName(value = "await-response", alternate = "await_response")
        AWAIT_RESPONSE,
        @SerializedName(value = "reuse-not-accepted", alternate = "reuse_not_accepted")
        REUSE_NOT_ACCEPTED,
        @SerializedName(value = "reuse-accepted", alternate = "reuse_accepted")
        REUSE_ACCEPTED,
        @SerializedName("done")
        DONE,
        @SerializedName("deleted")
        DELETED
    }

    public enum OOBRole {
        @SerializedName("sender")
        SENDER,
        @SerializedName("receiver")
        RECEIVER
    }
}
