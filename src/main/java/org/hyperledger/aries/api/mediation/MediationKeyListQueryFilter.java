/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.mediation;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @Builder
public class MediationKeyListQueryFilter implements AcaPyRequestFilter {
    @Builder.Default
    private Integer paginateLimit = -1;

    @Builder.Default
    private Integer paginateOffset = 0;
}
