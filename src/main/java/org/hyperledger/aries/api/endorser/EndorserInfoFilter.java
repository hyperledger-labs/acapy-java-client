/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.endorser;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @SuperBuilder
public class EndorserInfoFilter implements AcaPyRequestFilter {
    private String connId;
    private Boolean createTransactionForEndorser;
}
