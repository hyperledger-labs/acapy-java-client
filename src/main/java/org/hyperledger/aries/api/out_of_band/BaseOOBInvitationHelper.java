package org.hyperledger.aries.api.out_of_band;

import lombok.NonNull;
import org.hyperledger.acy_py.generated.model.InvitationRecord;
import org.hyperledger.aries.AriesClient;

import java.io.IOException;

public abstract class BaseOOBInvitationHelper {

    protected static final String NETWORK_ERROR = "aca-py is not available";

    protected final AriesClient acaPy;

    public BaseOOBInvitationHelper(@NonNull AriesClient acaPy) {
        super();
        this.acaPy = acaPy;
    }

    protected InvitationRecord createOOBInvitationWithAttachment(
            @NonNull String exchangeId,
            @NonNull AttachmentDef.AttachmentType attachmentType) throws IOException {
        return acaPy.outOfBandCreateInvitation(
                        InvitationCreateRequest.builder()
                                .usePublicDid(Boolean.TRUE)
                                .attachment(AttachmentDef.builder()
                                        .id(exchangeId)
                                        .type(attachmentType)
                                        .build())
                                .build(),
                        CreateInvitationFilter.builder()
                                .autoAccept(Boolean.TRUE)
                                .build())
                .orElseThrow();
    }
}
