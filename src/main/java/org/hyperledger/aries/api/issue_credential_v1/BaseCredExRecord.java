/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class BaseCredExRecord implements CredExStateTranslator {
    private Boolean autoIssue;
    private Boolean autoOffer;
    private Boolean autoRemove;
    private Boolean trace;

    private String connectionId;
    @SerializedName(value = "credential_exchange_id", alternate = { "cred_ex_id", "credExId", "credentialExchangeId" })
    private String credentialExchangeId;
    private String threadId;
    private String parentThreadId;

    private String createdAt;
    private String updatedAt;

    private CredentialExchangeInitiator initiator;
    private CredentialExchangeRole role;
    private CredentialExchangeState state;

    private String errorMsg;

    public boolean initiatorIsSelf() {
        return CredentialExchangeInitiator.SELF.equals(getInitiator());
    }

    public boolean initiatorIsExternal() {
        return CredentialExchangeInitiator.EXTERNAL.equals(getInitiator());
    }

    public boolean autoIssueEnabled() {
        return getAutoIssue() != null && getAutoIssue();
    }

    public boolean autoIssueOff() {
        return !autoIssueEnabled();
    }

    public boolean autoOfferEnabled() {
        return getAutoOffer() != null && getAutoOffer();
    }

    public boolean autoRemoveEnabled() {
        return getAutoRemove() != null && getAutoRemove();
    }

    public V1CredentialExchange.V1CredentialExchangeBuilder toV1Builder() {
        return V1CredentialExchange
                .builder()
                .autoIssue(autoIssue)
                .autoOffer(autoOffer)
                .autoRemove(autoRemove)
                .trace(trace)
                .connectionId(connectionId)
                .credentialExchangeId(credentialExchangeId)
                .threadId(threadId)
                .parentThreadId(parentThreadId)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .initiator(initiator)
                .role(role)
                .state(state);
    }
}
