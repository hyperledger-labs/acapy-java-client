/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.action_menu;

import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SendMenu {

    private Menu menu;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class Menu {
        private String description;
        @SerializedName("errormsg")
        private String errorMsg;
        private List<MenuOption> options;
        private String title;
    }
}
