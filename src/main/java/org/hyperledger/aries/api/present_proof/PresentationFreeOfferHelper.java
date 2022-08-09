/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.*;
import org.hyperledger.acy_py.generated.model.InvitationRecord;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.exception.AriesNetworkException;
import org.hyperledger.aries.api.out_of_band.AttachmentDef;
import org.hyperledger.aries.api.out_of_band.BaseOOBInvitationHelper;
import org.hyperledger.aries.api.present_proof_v2.*;

import java.io.IOException;

public class PresentationFreeOfferHelper extends BaseOOBInvitationHelper {

    @Builder
    public PresentationFreeOfferHelper(AriesClient acaPy) {
        super(acaPy);
    }

    public PresentationFreeOffer buildV1Indy(@NonNull PresentProofRequest.ProofRequest pr) {
        PresentationFreeOffer.PresentationFreeOfferBuilder r = PresentationFreeOffer.builder();
        try {
            PresentationExchangeRecord ex = acaPy.presentProofCreateRequest(PresentProofRequest.builder()
                            .proofRequest(pr)
                            .autoVerify(Boolean.TRUE)
                    .build()).orElseThrow();
            setAndBuildInvitation(r, ex);
        } catch (IOException e) {
            throw new AriesNetworkException(NETWORK_ERROR);
        }
        return r.build();
    }

    public PresentationFreeOffer buildV2Indy(@NonNull PresentProofRequest.ProofRequest pr) {
        PresentationFreeOffer.PresentationFreeOfferBuilder r = PresentationFreeOffer.builder();
        try {
            V20PresCreateRequestRequest req = V20PresCreateRequestRequest.builder()
                    .autoVerify(Boolean.TRUE)
                    .presentationRequest(V20PresSendRequestRequest.V20PresRequestByFormat.builder()
                            .indy(pr)
                            .build())
                    .build();
            PresentationExchangeRecord ex = acaPy.presentProofV2CreateRequest(req)
                    .map(V20PresExRecordToV1Converter::toV1)
                    .orElseThrow();
            setAndBuildInvitation(r, ex);
        } catch (IOException e) {
            throw new AriesNetworkException(NETWORK_ERROR);
        }
        return r.build();
    }

    public PresentationFreeOffer buildDif(@NonNull V2DIFProofRequest pr) {
        PresentationFreeOffer.PresentationFreeOfferBuilder r = PresentationFreeOffer.builder();
        try {
            V20PresCreateRequestRequest req = V20PresCreateRequestRequest.builder()
                    .autoVerify(Boolean.TRUE)
                    .presentationRequest(V20PresSendRequestRequest.V20PresRequestByFormat.builder()
                            .dif(pr)
                            .build())
                    .build();
            V20PresExRecord ex = acaPy.presentProofV2CreateRequest(req).orElseThrow();
            setAndBuildInvitation(r, ex);
        } catch (IOException e) {
            throw new AriesNetworkException(NETWORK_ERROR);
        }
        return r.build();
    }

    private void setAndBuildInvitation(PresentationFreeOffer.PresentationFreeOfferBuilder r, BasePresExRecord ex)
            throws IOException {
        r.presentationExchangeRecord(ex);
        InvitationRecord invitationRecord = createOOBInvitationWithAttachment(
                ex.getPresentationExchangeId(),
                AttachmentDef.AttachmentType.PRESENT_PROOF);
        r.invitationRecord(invitationRecord);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class PresentationFreeOffer {
        private BasePresExRecord presentationExchangeRecord;
        private InvitationRecord invitationRecord;
    }
}
