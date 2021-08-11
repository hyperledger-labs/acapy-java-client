package org.hyperledger.aries.api.credentials;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data
@Builder
public class CredentialRevokedFilter implements AcaPyRequestFilter {
    private String from;
    private String to;
}
