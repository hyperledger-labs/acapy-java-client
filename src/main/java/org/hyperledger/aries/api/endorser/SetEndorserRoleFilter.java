/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.endorser;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;
import org.hyperledger.aries.api.AcaPyRequestFilter;

@Data @Builder
public class SetEndorserRoleFilter implements AcaPyRequestFilter {
    private TransactionJobEnum transactionMyJob;

    public enum TransactionJobEnum {
        @SerializedName("TRANSACTION_AUTHOR")
        TRANSACTION_AUTHOR,

        @SerializedName("TRANSACTION_ENDORSER")
        TRANSACTION_ENDORSER,

        @SerializedName("reset")
        RESET
    }
}
