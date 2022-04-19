/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRole;

import javax.annotation.Nullable;

@Data @Builder
public class V2PresentProofRecordsFilter implements AcaPyRequestFilter {
    @Nullable private String connectionId;
    @Nullable private PresentationExchangeRole role;
    @Nullable private PresentProofRecordsFilterState state;
    @Nullable private String threadId;

    public enum PresentProofRecordsFilterState {
        @SerializedName("abandoned")
        ABANDONED,

        @SerializedName("done")
        DONE,

        @SerializedName("presentation-sent")
        PRESENTATIONS_SENT,

        @SerializedName("presentation-received")
        PRESENTATION_RECEIVED,

        @SerializedName("proposal-received")
        PROPOSAL_RECEIVED,

        @SerializedName("proposal-sent")
        PROPOSAL_SENT,

        @SerializedName("request-received")
        REQUEST_RECEIVED,

        @SerializedName("request-sent")
        REQUEST_SENT
    }
}
