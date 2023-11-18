/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.wallet;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum DefaultDidMethod {
    KEY("key"),
    SOV("sov");

    private String method;
}
