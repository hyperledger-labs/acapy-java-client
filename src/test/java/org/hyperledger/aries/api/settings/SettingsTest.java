/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.settings;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.IntegrationTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class SettingsTest extends IntegrationTestBase {

    @Test
    public void testSettings() throws IOException {
        Map<String, Object> settings = ac.settingsUpdate(UpdateProfileSettings.builder()
                .extraSetting("log-level", "info")
                .extraSetting("public-invites", Boolean.TRUE)
                .build())
                .orElseThrow();
        Assertions.assertEquals("info", settings.get("log.level"));
        settings = ac.settingsGet().orElseThrow();
        Assertions.assertEquals("info", settings.get("log.level"));
        Assertions.assertEquals("true", settings.get("public_invites"));
    }
}
