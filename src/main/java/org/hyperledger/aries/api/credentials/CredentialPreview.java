/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.credentials;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CredentialPreview {

    @SerializedName("@type")
    private String type = "issue-credential/1.0/credential-preview";

    private List<CredentialAttributes> attributes;

    public CredentialPreview(List<CredentialAttributes> attributes) {
        super();
        this.attributes = attributes;
    }
}
