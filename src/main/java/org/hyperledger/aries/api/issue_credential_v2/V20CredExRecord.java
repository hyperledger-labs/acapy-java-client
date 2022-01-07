/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
/*
 * aca-py client
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0.7.1
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.hyperledger.aries.api.issue_credential_v2;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hyperledger.acy_py.generated.model.V20CredIssue;
import org.hyperledger.acy_py.generated.model.V20CredPreview;
import org.hyperledger.acy_py.generated.model.V20CredRequest;
import org.hyperledger.aries.api.issue_credential_v1.*;

/**
 * V20CredExRecord
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true) @ToString(callSuper = true)
public class V20CredExRecord extends BaseCredExRecord {

    private V20CredExRecordByFormat byFormat;

    private V20CredOffer credOffer;
    private V20CredPreview credPreview;
    private V20CredProposal credProposal;
    private V20CredIssue credIssue;
    private V20CredRequest credRequest;

    public boolean payloadIsIndy() {
        return byFormat != null && byFormat.hasIndyPayload();
    }

    public boolean payloadIsLdProof() {
        return byFormat != null && byFormat.hasLdProof();
    }

    public V20CredExRecordByFormat.LdProof resolveLDCredOffer() {
        if (byFormat != null && byFormat.hasLdProof() && byFormat.getCredOffer() != null) {
            return byFormat.convertToLdProof(byFormat.getCredOffer());
        }
        return null;
    }

    public V20CredExRecordByFormat.LdProof resolveLDCredProposal() {
        if (byFormat != null && byFormat.hasLdProof() && byFormat.getCredProposal() != null) {
            return byFormat.convertToLdProof(byFormat.getCredProposal());
        }
        return null;
    }

    public V20CredExRecordByFormat.LdProof resolveLDCredRequest() {
        if (byFormat != null && byFormat.hasLdProof() && byFormat.getCredRequest() != null) {
            return byFormat.convertToLdProof(byFormat.getCredRequest());
        }
        return null;
    }

    public V20CredExRecordByFormat.LdProof resolveLDCredential() {
        if (byFormat != null && byFormat.hasLdProof() && byFormat.getCredIssue() != null) {
            return byFormat.convertToLdProof(byFormat.getCredIssue());
        }
        return null;
    }
}
