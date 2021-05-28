/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.action_menu;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MenuOption {
    private String description;
    private Boolean disabled;
    private MenuForm form;
    private String name;
    private String title;
}
