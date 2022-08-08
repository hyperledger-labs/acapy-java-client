package org.hyperledger.aries.api.present_proof;

import lombok.*;
import org.hyperledger.acy_py.generated.model.InvitationRecord;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.exception.AriesNetworkException;
import org.hyperledger.aries.api.out_of_band.AttachmentDef;
import org.hyperledger.aries.api.out_of_band.BaseOOBInvitationHelper;
import org.hyperledger.aries.api.present_proof_v2.V20PresCreateRequestRequest;
import org.hyperledger.aries.api.present_proof_v2.V20PresExRecordToV1Converter;
import org.hyperledger.aries.api.present_proof_v2.V20PresSendRequestRequest;

import java.io.IOException;

public class PresentationFreeOfferHelper extends BaseOOBInvitationHelper {

    @Builder
    public PresentationFreeOfferHelper(@NonNull AriesClient acaPy) {
        super(acaPy);
    }

    public PresentationFreeOffer buildV1Indy(@NonNull PresentProofRequest.ProofRequest pr) {
        PresentationFreeOffer.PresentationFreeOfferBuilder r = PresentationFreeOffer.builder();
        try {
            PresentationExchangeRecord ex = acaPy.presentProofCreateRequest(PresentProofRequest.builder()
                            .proofRequest(pr)
                            .autoVerify(Boolean.TRUE)
                    .build()).orElseThrow();
            r.presentationExchangeRecord(ex);
            InvitationRecord invitationRecord = createOOBInvitationWithAttachment(
                    ex.getPresentationExchangeId(),
                    AttachmentDef.AttachmentType.PRESENT_PROOF);
            r.invitationRecord(invitationRecord);
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
            r.presentationExchangeRecord(ex);
            InvitationRecord invitationRecord = createOOBInvitationWithAttachment(
                    ex.getPresentationExchangeId(),
                    AttachmentDef.AttachmentType.PRESENT_PROOF);
            r.invitationRecord(invitationRecord);
        } catch (IOException e) {
            throw new AriesNetworkException(NETWORK_ERROR);
        }
        return r.build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static final class PresentationFreeOffer {
        private PresentationExchangeRecord presentationExchangeRecord;
        private InvitationRecord invitationRecord;
    }
}
