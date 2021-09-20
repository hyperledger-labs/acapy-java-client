/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v2;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.acy_py.generated.model.V20CredExRecord;
import org.hyperledger.aries.api.AcaPyRequestFilter;
import org.hyperledger.aries.api.issue_credential_v1.CredentialExchangeRole;

import javax.annotation.Nullable;

@Data @Builder
public class V2IssueCredentialRecordsFilter implements AcaPyRequestFilter {
    @Nullable private String connectionId;
    @Nullable private CredentialExchangeRole role;
    @Nullable private V20CredExRecord.StateEnum state;
    @Nullable private String threadId;
}
