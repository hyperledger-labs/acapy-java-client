/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateInvitationParams implements AcaPyRequestFilter {
    private String alias;
    private Boolean autoAccept;
    private Boolean multiUse;
    private Boolean isPublic;
}
