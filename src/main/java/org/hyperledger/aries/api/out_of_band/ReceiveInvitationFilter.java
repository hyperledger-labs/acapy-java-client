package org.hyperledger.aries.api.out_of_band;

import org.hyperledger.aries.api.AcaPyRequestFilter;

public class ReceiveInvitationFilter implements AcaPyRequestFilter {
    private String alias;
    private Boolean autoAccept;
    private String mediationId;
    private Boolean useExistingConnection;
}
