/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.credentials.CredentialPreview;

@Data @NoArgsConstructor
public class V1CredentialCreate {
    private Boolean autoRemove;
    private String comment;
    private String credDefId ;
    private CredentialPreview credentialProposal;
    private String issuerDid;
    private String schemaId;
    private String schemaIssuerDid;
    private String schemaName;
    private String schemaVersion;
    private Boolean trace;
}
