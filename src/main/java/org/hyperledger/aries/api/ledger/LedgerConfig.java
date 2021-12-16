/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.ledger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hyperledger.acy_py.generated.model.LedgerConfigInstance;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
public class LedgerConfig {
    private List<LedgerConfigInstance> productionLedgers;
    private List<LedgerConfigInstance> nonProductionLedgers;
}
