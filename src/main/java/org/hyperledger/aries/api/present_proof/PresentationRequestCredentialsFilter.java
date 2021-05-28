/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import okhttp3.HttpUrl;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.aries.api.AcaPyRequestFilter;

import java.util.Iterator;
import java.util.List;

@Data @Builder
public class PresentationRequestCredentialsFilter implements AcaPyRequestFilter {

    /** Maximum number to retrieve */
    private String count;

    /** (JSON) object mapping referents to extra WQL queries */
    private String extraQuery;

    /** Proof request referents of interest */
    private List<String> referent;

    /** start index */
    private String start;

    @Override
    public HttpUrl.Builder buildParams(@NonNull HttpUrl.Builder b) {
        if (StringUtils.isNotEmpty(count)) {
            b.addQueryParameter("count", count);
        }
        if (StringUtils.isNotEmpty(extraQuery)) {
            b.addQueryParameter("extra_query", extraQuery);
        }
        if (referent != null && referent.size() > 0) {
            StringBuilder sb = new StringBuilder();
            Iterator<String> it = referent.stream().iterator();
            while(it.hasNext()) {
                sb.append(it.next());
                if (it.hasNext()) {
                    sb.append(",");
                }
            }
            b.addQueryParameter("referent", sb.toString());
        }
        if (StringUtils.isNotEmpty(start)) {
            b.addQueryParameter("start", start);
        }
        return b;
    }
}
