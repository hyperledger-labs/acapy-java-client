/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.server;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class AdminStatusReadiness {
    private Boolean ready;

    public boolean isReady() {
        return Boolean.TRUE.equals(ready);
    }
}
