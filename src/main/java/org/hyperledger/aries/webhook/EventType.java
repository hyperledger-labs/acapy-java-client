/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * List of aca-py webhook/websocket event types/topics.
 */
@AllArgsConstructor
public enum EventType {

    BASIC_MESSAGES("basicmessages"),
    CONNECTIONS("connections"),
    DISCOVER_FEATURE("discover_feature"),
    ENDORSE_TRANSACTION("endorse_transaction"),
    ISSUER_CRED_REV("issuer_cred_rev"),
    ISSUE_CREDENTIAL("issue_credential"),
    ISSUE_CREDENTIAL_V2("issue_credential_v2_0"),
    ISSUE_CREDENTIAL_V2_INDY("issue_credential_v2_0_indy"),
    ISSUE_CREDENTIAL_V2_LD_PROOF("issue_credential_v2_0_ld_proof"),
    OUT_OF_BAND("out_of_band"),
    PING("ping"),
    PRESENT_PROOF("present_proof"),
    PRESENT_PROOF_V2("present_proof_v2_0"),
    PROBLEM_REPORT("problem_report"),
    REVOCATION_NOTIFICATION("revocation-notification"),
    REVOCATION_NOTIFICATION_V2("revocation-notification-v2"),
    REVOCATION_REGISTRY("revocation_registry"),
    SETTINGS("settings")
    ;

    @Getter
    private final String topic;

    public boolean topicEquals(String other) {
        return StringUtils.equals(topic, other);
    }

    @Override
    public String toString() {
        return this.topic;
    }

    public static Optional<EventType> fromTopic(String topic) {
        return Arrays.stream(EventType.values())
                .filter(e -> StringUtils.equalsIgnoreCase(e.getTopic(), topic))
                .findFirst();
    }
}