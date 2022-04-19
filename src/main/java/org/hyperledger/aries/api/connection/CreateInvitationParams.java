/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import lombok.*;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.api.AcaPyRequestFilter;

import java.util.Locale;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateInvitationParams implements AcaPyRequestFilter {
    private String alias;
    private Boolean autoAccept;
    private Boolean multiUse;
    private Boolean isPublic;
}
