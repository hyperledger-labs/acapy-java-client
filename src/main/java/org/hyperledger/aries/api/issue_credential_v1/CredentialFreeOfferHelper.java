/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.acy_py.generated.model.InvitationRecord;
import org.hyperledger.acy_py.generated.model.V20CredFilterIndy;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.credentials.CredentialPreview;
import org.hyperledger.aries.api.exception.AriesNetworkException;
import org.hyperledger.aries.api.issue_credential_v2.V20CredExRecord;
import org.hyperledger.aries.api.issue_credential_v2.V2CredentialExchangeFree;
import org.hyperledger.aries.api.issue_credential_v2.V2ToV1IndyCredentialConverter;
import org.hyperledger.aries.api.out_of_band.AttachmentDef;
import org.hyperledger.aries.api.out_of_band.BaseOOBInvitationHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Helper class that creates an out-of-band invitation with an attached credential
 * offer for v1 or v2 credential exchanges.It uses two api requests:
 * 1. /issue-credential/create-offer
 * 2. /out-of-band/create-invitation
 * Like it is with the connection-less proof-request the barcode only points to a temporary URL which then
 * redirects to another URL in the location header that then
 * contains the credential offer in the m parameter like <code>https://mydomain.com?m=ey...</code>
 * The base64 encoded url can be found in the invitationRecord.invitationUrl
 */
@Slf4j
public class CredentialFreeOfferHelper extends BaseOOBInvitationHelper {

    @Builder
    public CredentialFreeOfferHelper(AriesClient acaPy) {
        super(acaPy);
    }

    /**
     * Build for v1/indy
     * @param credentialDefinitionId credential definition id
     * @param document credential document
     * @return {@link CredentialFreeOffer}
     */
    public CredentialFreeOffer buildV1Indy(
            @NonNull String credentialDefinitionId,
            @NonNull Map<String, String> document) {
        return getCredentialFreeOffer(credentialDefinitionId, CredentialAttributes.fromMap(document));
    }

    /**
     * Build for v1/indy
     * @param credentialDefinitionId credential definition id
     * @param document list of credential attributes
     * @return {@link CredentialFreeOffer}
     */
    public CredentialFreeOffer buildV1Indy(
            @NonNull String credentialDefinitionId,
            @NonNull List<CredentialAttributes> document) {
        return getCredentialFreeOffer(credentialDefinitionId, document);
    }

    /**
     * Build for v2/indy
     * @param credentialDefinitionId credential definition id
     * @param document credential document
     * @return {@link CredentialFreeOffer}
     */
    public CredentialFreeOffer buildV2Indy(
            @NonNull String credentialDefinitionId,
            @NonNull Map<String, String> document) {
        return getCredentialFreeOfferV2(credentialDefinitionId, CredentialAttributes.fromMap(document));
    }

    /**
     * Build for v2/indy
     * @param credentialDefinitionId credential definition id
     * @param document list of credential attributes
     * @return {@link CredentialFreeOffer}
     */
    public CredentialFreeOffer buildV2Indy(
            @NonNull String credentialDefinitionId,
            @NonNull List<CredentialAttributes> document) {
        return getCredentialFreeOfferV2(credentialDefinitionId, document);
    }

    /**
     * Build for dif verifiable credential
     * @param vc {@link V2CredentialExchangeFree.LDProofVCDetail}
     * @return {@link CredentialFreeOffer}
     */
    public CredentialFreeOffer buildDif(@NonNull V2CredentialExchangeFree.LDProofVCDetail vc) {
        CredentialFreeOffer.CredentialFreeOfferBuilder r = CredentialFreeOffer.builder();

        try {
            V2CredentialExchangeFree create = V2CredentialExchangeFree.builder()
                    .autoIssue(Boolean.TRUE)
                    .autoRemove(Boolean.TRUE)
                    .filter(V2CredentialExchangeFree.V20CredFilter.builder()
                            .ldProof(vc)
                            .build())
                    .build();
            V20CredExRecord ex = acaPy.issueCredentialV2CreateOffer(create).orElseThrow();
            setAndBuildInvitation(r, ex);
        } catch (IOException e) {
            throw new AriesNetworkException(NETWORK_ERROR);
        }
        return r.build();
    }

    private CredentialFreeOffer getCredentialFreeOffer(@NonNull String credentialDefinitionId, @NonNull List<CredentialAttributes> document) {
        CredentialFreeOffer.CredentialFreeOfferBuilder r = CredentialFreeOffer.builder();

        try{
            // issue-credential/create in conjunction with oob invitation attachment
            // step 1 - create credential offer
            V1CredentialFreeOfferRequest create = V1CredentialFreeOfferRequest.builder()
                    .autoIssue(Boolean.TRUE)
                    .autoRemove(Boolean.TRUE)
                    .credDefId(credentialDefinitionId)
                    .credentialPreview(new CredentialPreview(document))
                    .build();
            V1CredentialExchange ex = acaPy.issueCredentialCreateOffer(create).orElseThrow();
            // step 2 - create out-of-band invitation with attached credential offer
            setAndBuildInvitation(r, ex);
        } catch (IOException e) {
            throw new AriesNetworkException(NETWORK_ERROR);
        }
        return r.build();
    }

    private CredentialFreeOffer getCredentialFreeOfferV2(@NonNull String credentialDefinitionId, @NonNull List<CredentialAttributes> document) {
        CredentialFreeOffer.CredentialFreeOfferBuilder r = CredentialFreeOffer.builder();
        try {
            V2CredentialExchangeFree create = V2CredentialExchangeFree.builder()
                    .autoIssue(Boolean.TRUE)
                    .autoRemove(Boolean.TRUE)
                    .filter(V2CredentialExchangeFree.V20CredFilter.builder()
                            .indy(V20CredFilterIndy.builder()
                                    .credDefId(credentialDefinitionId)
                                    .build())
                            .build())
                    .credentialPreview(V2CredentialExchangeFree.V2CredentialPreview.builder()
                            .attributes(document)
                            .build())
                    .build();
            V1CredentialExchange ex = acaPy.issueCredentialV2CreateOffer(create)
                    .map(V2ToV1IndyCredentialConverter::toV1Offer)
                    .orElseThrow();
            setAndBuildInvitation(r, ex);
        } catch (IOException e) {
            throw new AriesNetworkException(NETWORK_ERROR);
        }
        return r.build();
    }

    private void setAndBuildInvitation(
            @NonNull CredentialFreeOffer.CredentialFreeOfferBuilder r,
            @NonNull BaseCredExRecord ex)
            throws IOException {
        r.credentialExchangeRecord(ex);
        InvitationRecord invitationRecord = createOOBInvitationWithAttachment(
                ex.getCredentialExchangeId(),
                AttachmentDef.AttachmentType.CREDENTIAL_OFFER);
        r.invitationRecord(invitationRecord);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class CredentialFreeOffer {
        private BaseCredExRecord credentialExchangeRecord;
        private InvitationRecord invitationRecord;
    }
}
