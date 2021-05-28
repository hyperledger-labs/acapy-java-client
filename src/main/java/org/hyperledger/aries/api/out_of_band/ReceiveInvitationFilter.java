package org.hyperledger.aries.api.out_of_band;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @Builder
public class ReceiveInvitationFilter implements AcaPyRequestFilter {
    private String alias;
    private Boolean autoAccept;
    private String mediationId;
    private Boolean useExistingConnection;
}
