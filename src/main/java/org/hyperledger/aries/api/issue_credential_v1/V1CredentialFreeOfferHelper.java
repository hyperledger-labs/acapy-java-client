/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.acy_py.generated.model.InvitationRecord;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.credentials.CredentialPreview;
import org.hyperledger.aries.api.out_of_band.AttachmentDef;
import org.hyperledger.aries.api.out_of_band.CreateInvitationFilter;
import org.hyperledger.aries.api.out_of_band.InvitationCreateRequest;

import java.io.IOException;
import java.util.Map;

/**
 * Helper class that creates a out-of-band invitation with an attached credential offer.
 * It is uses of two api requests:
 * 1. /issue-credential/create-offer
 * 2. /out-of-band/create-invitation
 * Like it is with the connection-less proof-request the barcode only points to a temporary URL which then
 * redirects to another URL in the location header that then
 * contains the credential offer in the m parameter like https://mydomain.com?m=ey...
 * The base64 encoded url can be found in the invitationRecord.invitationUrl
 */
@Slf4j
public class V1CredentialFreeOfferHelper {

    private final AriesClient acaPy;

    public V1CredentialFreeOfferHelper(AriesClient acaPy) {
        super();
        this.acaPy = acaPy;
    }

    public CredentialFreeOffer buildFreeOffer(@NonNull String credentialDefinitionId, Map<String, String> document) {
        CredentialFreeOffer.CredentialFreeOfferBuilder r = CredentialFreeOffer.builder();
        try{
            // issue-credential/create in conjunction with oob invitation attachment
            // step 1 - create credential offer
            V1CredentialFreeOfferRequest create = V1CredentialFreeOfferRequest
                    .builder()
                    .autoIssue(Boolean.TRUE)
                    .autoRemove(Boolean.TRUE)
                    .credDefId(credentialDefinitionId)
                    .credentialPreview(new CredentialPreview(CredentialAttributes.fromMap(document)))
                    .build();
            V1CredentialExchange ex = acaPy.issueCredentialCreateOffer(create).orElseThrow();
            r.threadId(ex.getThreadId());
            r.credentialExchangeId(ex.getCredentialExchangeId());
            r.credentialProposalDict(ex.getCredentialProposalDict());
            // step 2 - create out-of-band invitation with attached credential offer
            InvitationRecord invitationRecord = acaPy.outOfBandCreateInvitation(
                    InvitationCreateRequest.builder()
                            .usePublicDid(Boolean.TRUE)
                            .attachment(AttachmentDef.builder()
                                    .id(ex.getCredentialExchangeId())
                                    .type(AttachmentDef.AttachmentType.CREDENTIAL_OFFER)
                                    .build())
                    .build(),
                    CreateInvitationFilter.builder()
                            .autoAccept(Boolean.TRUE)
                            .build()).orElseThrow();
            r.invitationRecord(invitationRecord);
        } catch (IOException e) {
            log.error("aca-py is not available", e);
        }
        return r.build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class CredentialFreeOffer {

        /** reference to the credential exchange record */
        private String credentialExchangeId;

        /** credential exchange thread id */
        private String threadId;

        private V1CredentialExchange.CredentialProposalDict credentialProposalDict;

        private InvitationRecord invitationRecord;
    }
}
