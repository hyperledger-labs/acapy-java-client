/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.settings;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Websocket event payload
 *
 * <pre>
 * {@code
 * "payload": {
 *     "authenticated": true,
 *     "label": "Aries Cloud Agent",
 *     "endpoint": "http://localhost:8030",
 *     "no_receive_invites": false,
 *     "help_link": null
 * }
 * }
 * </pre>
 */
@Data
@NoArgsConstructor
public class Settings {
    private boolean authenticated;
    private boolean noReceiveInvites;
    private String label;
    private String endpoint;
    private String helpLink;
}
