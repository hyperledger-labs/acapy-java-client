/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.did_exchange;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DIDXRequest {

    @SerializedName("@id")
    private String id;

    @SerializedName("@type")
    private String type;

    @SerializedName("~thread")
    private DIDXThread thread;

    private String did;

    @SerializedName("did_doc~attach")
    private DIDXRequestDidDocAttach didDocAttach;

    private String label;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static final class DIDXThread {
        private String thid;
        private String pthid;
    }
}
