/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import org.hyperledger.aries.api.credentials.CredentialPreview;
import org.hyperledger.aries.config.CredDefId;

import javax.annotation.Nullable;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class V1CredentialOfferRequest {

    private Boolean autoIssue;

    /**
     *  Whether to remove the credential exchange record on completion
     *  (overrides --preserve-exchange-records configuration setting)
     */
    @Nullable
    private Boolean autoRemove;

    @Nullable
    private String comment;

    @NonNull
    private String connectionId;

    @Nullable
    @SerializedName(value = CredDefId.CREDENTIAL_DEFINITION_ID, alternate = CredDefId.CRED_DEF_ID)
    private String credentialDefinitionId;

    @Nullable
    private CredentialPreview credentialPreview;

    @Nullable
    private Boolean trace;
}
