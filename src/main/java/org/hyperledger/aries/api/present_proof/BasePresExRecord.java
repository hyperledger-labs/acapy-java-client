/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hyperledger.aries.api.ExchangeVersion;

import java.util.List;

/**
 * Keeps track of fields that are common to both V1 abd V2 presentation exchanges.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BasePresExRecord implements PresExStateTranslator {

    private String createdAt;
    private String updatedAt;

    private Boolean autoPresent;
    private Boolean autoRemove;
    private Boolean autoVerify;
    private Boolean trace;
    private Boolean verified;
    /**
     * Proof verification warning or error information.
     */
    // TODO: why not use errorMsg?
    private List<String> verifiedMsgs;
    private String errorMsg;

    private String connectionId;
    @SerializedName(value = "presentation_exchange_id", alternate = { "pres_ex_id", "presExId", "presentationExchangeId" })
    private String presentationExchangeId;
    private String threadId;

    private PresentationExchangeInitiator initiator;
    private PresentationExchangeRole role;
    private PresentationExchangeState state;

    @JsonIgnore
    public String getPresExId() {
        return this.presentationExchangeId;
    }

    public boolean isVerified() {
        return Boolean.TRUE.equals(verified);
    }

    public boolean isAutoPresent() {
        return Boolean.TRUE.equals(autoPresent);
    }

    public boolean isNotAutoPresent() {
        return !isAutoPresent();
    }

    public boolean isAutoVerify() {
        return Boolean.TRUE.equals(autoVerify);
    }

    public boolean isNotAutoVerify() {
        return !isAutoVerify();
    }

    public boolean initiatorIsSelf() {
        return PresentationExchangeInitiator.SELF.equals(getInitiator());
    }

    public boolean initiatorIsExternal() {
        return PresentationExchangeInitiator.EXTERNAL.equals(getInitiator());
    }

    public boolean versionIsV1() {
        return ExchangeVersion.V1.equals(getVersion());
    }

    public abstract ExchangeVersion getVersion();
}
