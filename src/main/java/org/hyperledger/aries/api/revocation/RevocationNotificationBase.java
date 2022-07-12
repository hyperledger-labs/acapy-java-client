/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.revocation;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public abstract class RevocationNotificationBase {

    RevocationInfo expressionToRevocationInfo(@NonNull String expression, @NonNull Integer expLength) {
        RevocationInfo.RevocationInfoBuilder b = RevocationInfo.builder();
        if (StringUtils.isNotEmpty(expression)) {
            String[] parts = expression.split("::");
            if (parts.length != expLength) {
                throw new IllegalArgumentException("Not a valid revocation notification");
            }
            b.revRegId(parts[expLength - 2]);
            b.credRevId(parts[expLength - 1]);
        }
        return b.build();
    }

    abstract RevocationInfo toRevocationInfo();

    @Data
    @Builder
    public static final class RevocationInfo {
        /** revocation-registry-id */
        private String revRegId;
        /** credential-revocation-id */
        private String credRevId;
    }
}
