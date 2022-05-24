/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof_v2;

import lombok.NonNull;
import org.hyperledger.aries.api.ExchangeVersion;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;

public class V20PresExRecordToV1Converter {

    public static PresentationExchangeRecord toV1(@NonNull V20PresExRecord v2) {
        return PresentationExchangeRecord
                .builder()
                .autoPresent(v2.getAutoPresent())
                .createdAt(v2.getCreatedAt())
                .updatedAt(v2.getUpdatedAt())
                .trace(v2.getTrace())
                .errorMsg(v2.getErrorMsg())
                .verified(v2.getVerified())
                .connectionId(v2.getConnectionId())
                .presentationExchangeId(v2.getPresExId())
                .threadId(v2.getThreadId())
                .initiator(v2.getInitiator())
                .state(v2.getState())
                .role(v2.getRole())
                .presentationRequest(v2.resolveIndyPresentationRequest())
                .presentation(v2.resolveIndyPresentation())
                .identifiers(v2.resolveIndyIdentifiers())
                .version(ExchangeVersion.V2)
                .build();
    }
}
