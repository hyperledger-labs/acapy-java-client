/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.out_of_band;

import com.google.gson.JsonElement;
import org.hyperledger.aries.config.GsonConfig;

public interface InvitationMessageTranslator {

    JsonElement getInvitation();

    default InvitationMessage<String> asStringType() {
        return GsonConfig.defaultConfig().fromJson(getInvitation(), InvitationMessage.STRING_TYPE);
    }

    default  InvitationMessage<InvitationMessage.InvitationMessageService> asRFC0067Type() {
        return GsonConfig.defaultConfig().fromJson(getInvitation(), InvitationMessage.RFC0067_TYPE);
    }
}
