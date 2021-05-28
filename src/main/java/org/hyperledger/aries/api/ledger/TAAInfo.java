/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.ledger;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data @NoArgsConstructor
public class TAAInfo {

    private AMLRecord amlRecord;

    private TAAAcceptance taaAccepted;

    private TAARecord taaRecord;

    private Boolean taaRequired;

    @Data @NoArgsConstructor
    public static final class AMLRecord {
        private Map<String, String> aml;
        @SerializedName("amlContext")
        private String amlContext;
        private String version;
    }

    @Data @NoArgsConstructor
    public static final class TAAAcceptance {
        private String mechanism;
        private Integer time;
    }

    @Data @NoArgsConstructor
    public static final class TAARecord {
        private String digest;
        private String text;
        private String version;
    }
}
