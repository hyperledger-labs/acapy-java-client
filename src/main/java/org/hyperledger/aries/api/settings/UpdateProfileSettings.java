/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.settings;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.Map;

@Builder @Data
public class UpdateProfileSettings {
    @Singular
    private Map<String, Object> extraSettings;
}
