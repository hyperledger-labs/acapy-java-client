/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.mediation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

import java.util.List;

@Data @Builder
public class MediationRequestsFilter implements AcaPyRequestFilter {
    private String connId;
    private List<String> mediatorTerms;
    private List<String> recipientTerms;
    private MediationState state;

    public enum MediationState {
        @JsonProperty("request")
        @SerializedName("request")
        REQUEST,

        @JsonProperty("granted")
        @SerializedName("granted")
        GRANTED,

        @JsonProperty("denied")
        @SerializedName("denied")
        DENIED
    }
}
