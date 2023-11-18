/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.wallet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.api.endorser.EndorserInfoFilter;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AssignPublicDidFilter extends EndorserInfoFilter {
    private String mediationId;

    @Override
    public HttpUrl.Builder buildParams(@NonNull HttpUrl.Builder b) {
        if (Boolean.TRUE.equals(getCreateTransactionForEndorser())) {
            b.addQueryParameter("create_transaction_for_endorser", getCreateTransactionForEndorser().toString());
        }
        if (StringUtils.isNotEmpty(getConnId())) {
            b.addQueryParameter("conn_id", getConnId());
        }
        if (StringUtils.isNotEmpty(mediationId))  {
            b.addQueryParameter("mediation_id", mediationId);
        }
        return b;
    }
}
