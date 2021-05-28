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
            b.addQueryParameter("auto_accept", autoAccept.toString());
        }
        if (multiUse != null) {
            b.addQueryParameter("multi_use", multiUse.toString());
        }
        if (isPublic != null) {
            b.addQueryParameter("public", isPublic.toString());
        }
        return b;
    }
}
