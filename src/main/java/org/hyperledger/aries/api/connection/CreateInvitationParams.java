/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.connection;

import lombok.*;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CreateInvitationParams {
    private String alias;
    private Boolean autoAccept;
    private Boolean multiUse;
    private Boolean isPublic;

    public HttpUrl.Builder buildParams(@NonNull HttpUrl.Builder b) {
        if (StringUtils.isNotEmpty(alias)) {
            b.addQueryParameter("alias", alias);
        }
        if (autoAccept != null) {
            b.addQueryParameter("auto_accept", toLowerCase(autoAccept));
        }
        if (multiUse != null) {
            b.addQueryParameter("multi_use", toLowerCase(multiUse));
        }
        if (isPublic != null) {
            b.addQueryParameter("public", toLowerCase(isPublic));
        }
        return b;
    }

    private String toLowerCase(@NonNull Boolean val) {
        return val.toString().toLowerCase(Locale.US);
    }
}
