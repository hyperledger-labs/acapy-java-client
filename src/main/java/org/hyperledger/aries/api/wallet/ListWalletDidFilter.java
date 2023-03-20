/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.wallet;

import lombok.Builder;
import lombok.Data;
import org.hyperledger.acy_py.generated.model.DID;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data
@Builder
public class ListWalletDidFilter implements AcaPyRequestFilter {
    /** did of interest */
    private String did;

    /** Key type to query for */
    private DID.KeyTypeEnum keyType;

    /** did method to query for */
    private String method;

    /** Whether did is current public, or wallet only */
    private DID.PostureEnum posture;

    /** verification key of interest */
    private String verkey;
}
