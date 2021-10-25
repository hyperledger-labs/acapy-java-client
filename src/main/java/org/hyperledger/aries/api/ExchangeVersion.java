/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api;

public enum ExchangeVersion {
    /** credential or proof exchange version 1 */
    V1,
    /** credential or proof exchange version 2 */
    V2
    ;

    public boolean isV1() {
        return this.equals(ExchangeVersion.V1);
    }

    public boolean isV2() {
        return this.equals(ExchangeVersion.V2);
    }
}
