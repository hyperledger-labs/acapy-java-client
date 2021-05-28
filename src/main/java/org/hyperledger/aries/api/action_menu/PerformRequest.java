/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.action_menu;

import lombok.*;

import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PerformRequest {
    private String name;
    private Map<String, String> params;
}
