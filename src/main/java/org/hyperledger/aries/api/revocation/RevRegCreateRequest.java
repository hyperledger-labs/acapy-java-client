/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import lombok.*;

import javax.annotation.Nullable;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RevRegCreateRequest {
    @NonNull private String credentialDefinitionId;
    /** Maximum credentials, minimum 4, maximum 32768 */
    @Nullable private Integer maxCredNum;
}
