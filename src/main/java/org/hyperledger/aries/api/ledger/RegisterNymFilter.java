/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.ledger;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @Builder
public final class RegisterNymFilter implements AcaPyRequestFilter {
    @NonNull
    private String did;
    @NonNull
    private String verkey;
    private String alias;
    private String connId;
    private Boolean createTransactionForEndorser;
    private IndyLedgerRoles role;
}
