/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.discover_features.DiscoverFeatureEvent;
import org.hyperledger.aries.api.endorser.EndorseTransactionRecord;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.api.issue_credential_v2.V20CredExRecord;
import org.hyperledger.aries.api.issue_credential_v2.V2IssueIndyCredentialEvent;
import org.hyperledger.aries.api.issue_credential_v2.V2IssueLDCredentialEvent;
import org.hyperledger.aries.api.message.BasicMessage;
import org.hyperledger.aries.api.message.ProblemReport;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof_v2.V20PresExRecord;
import org.hyperledger.aries.api.revocation.RevocationEvent;
import org.hyperledger.aries.api.revocation.RevocationNotificationEvent;
import org.hyperledger.aries.api.trustping.PingEvent;

/**
 * Event handler for single tenant wallets, the walletId will be ignored,
 */
@Slf4j
public abstract class EventHandler implements IEventHandler {

    private static final String LOG_MSG_SINGLE = "topic: {}, payload: {}";

    private final EventParser parser = new EventParser();

    public void handleEvent(String topic, String payload) {
        handleEvent(null, topic, payload);
    }

    public void handleEvent(String walletId, String topic, String payload) {

        handleRaw(topic, payload);

        try {
            if (EventType.CONNECTIONS.valueEquals(topic)) {
                parser.parseValueSave(payload, ConnectionRecord.class).ifPresent(this::handleConnection);
            } else if (EventType.PRESENT_PROOF.valueEquals(topic)) {
                parser.parsePresentProof(payload).ifPresent(this::handleProof);
            } else if (EventType.PRESENT_PROOF_V2.valueEquals(topic)) {
                parser.parseValueSave(payload, V20PresExRecord.class).ifPresent(this::handleProofV2);
            } else if (EventType.ISSUE_CREDENTIAL.valueEquals(topic)) {
                parser.parseValueSave(payload, V1CredentialExchange.class).ifPresent(this::handleCredential);
            } else if (EventType.ISSUE_CREDENTIAL_V2.valueEquals(topic)) {
                parser.parseValueSave(payload, V20CredExRecord.class).ifPresent(this::handleCredentialV2);
            } else if (EventType.ISSUE_CREDENTIAL_V2_INDY.valueEquals(topic)) {
                parser.parseValueSave(payload, V2IssueIndyCredentialEvent.class).ifPresent(this::handleIssueCredentialV2Indy);
            } else if (EventType.ISSUE_CREDENTIAL_V2_LD_PROOF.valueEquals(topic)) {
                parser.parseValueSave(payload, V2IssueLDCredentialEvent.class).ifPresent(this::handleIssueCredentialV2LD);
            } else if (EventType.BASIC_MESSAGES.valueEquals(topic)) {
                parser.parseValueSave(payload, BasicMessage.class).ifPresent(this::handleBasicMessage);
            } else if (EventType.PING.valueEquals(topic)) {
                // WebSocket ping events may not have a payload
                if (payload == null) handlePing(null);
                else parser.parseValueSave(payload, PingEvent.class).ifPresent(this::handlePing);
            } else if (EventType.ISSUER_CRED_REV.valueEquals(topic)) {
                parser.parseValueSave(payload, RevocationEvent.class).ifPresent(this::handleRevocation);
            } else if (EventType.ENDORSE_TRANSACTION.valueEquals(topic)) {
                parser.parseValueSave(payload, EndorseTransactionRecord.class).ifPresent(this::handleEndorseTransaction);
            } else if (EventType.PROBLEM_REPORT.valueEquals(topic)) {
                parser.parseValueSave(payload, ProblemReport.class).ifPresent(this::handleProblemReport);
            } else if (EventType.DISCOVER_FEATURE.valueEquals(topic)) {
                parser.parseValueSave(payload, DiscoverFeatureEvent.class).ifPresent(this::handleDiscoverFeature);
            } else if (EventType.REVOCATION_NOTIFICATION.valueEquals(topic)) {
                parser.parseValueSave(payload, RevocationNotificationEvent.class).ifPresent(this::handleRevocationNotification);
            }
        } catch (Throwable e) {
            log.error("Error in webhook event handler:", e);
        }
    }

    public void handleConnection(ConnectionRecord connection) {
        log.debug(LOG_MSG_SINGLE, EventType.CONNECTIONS, connection);
    }

    public void handleProof(PresentationExchangeRecord proof) {
        log.debug(LOG_MSG_SINGLE, EventType.PRESENT_PROOF, proof);
    }

    public void handleProofV2(V20PresExRecord proof) {
        log.debug(LOG_MSG_SINGLE, EventType.PRESENT_PROOF_V2, proof);
    }

    public void handleCredential(V1CredentialExchange credential) {
        log.debug(LOG_MSG_SINGLE, EventType.ISSUE_CREDENTIAL, credential);
    }

    public void handleCredentialV2(V20CredExRecord v20Credential) {
        log.debug(LOG_MSG_SINGLE, EventType.ISSUE_CREDENTIAL_V2, v20Credential);
    }

    public void handleDiscoverFeature(DiscoverFeatureEvent discoverFeature) {
        log.debug(LOG_MSG_SINGLE, EventType.DISCOVER_FEATURE, discoverFeature);
    }

    public void handleIssueCredentialV2Indy(V2IssueIndyCredentialEvent credentialInfo) {
        log.debug(LOG_MSG_SINGLE, EventType.ISSUE_CREDENTIAL_V2_INDY, credentialInfo);
    }

    public void handleIssueCredentialV2LD(V2IssueLDCredentialEvent credentialInfo) {
        log.debug(LOG_MSG_SINGLE, EventType.ISSUE_CREDENTIAL_V2_LD_PROOF, credentialInfo);
    }

    public void handleBasicMessage(BasicMessage message) {
        log.debug(LOG_MSG_SINGLE, EventType.BASIC_MESSAGES, message);
    }

    public void handlePing(PingEvent ping) {
        log.debug(LOG_MSG_SINGLE, EventType.PING, ping);
    }

    public void handleRevocation(RevocationEvent revocation) {
        log.debug(LOG_MSG_SINGLE, EventType.ISSUER_CRED_REV, revocation);
    }

    public void handleRevocationNotification(RevocationNotificationEvent revocationNotification) {
        log.debug(LOG_MSG_SINGLE, EventType.REVOCATION_NOTIFICATION, revocationNotification);
    }

    public void handleEndorseTransaction(EndorseTransactionRecord transaction) {
        log.debug(LOG_MSG_SINGLE, EventType.ENDORSE_TRANSACTION, transaction);
    }

    public void handleProblemReport(ProblemReport report) {
        log.debug(LOG_MSG_SINGLE, EventType.PROBLEM_REPORT, report);
    }

    public void handleRaw(String eventType, String json) {
        if (log.isTraceEnabled()) {
            log.trace(LOG_MSG_SINGLE, eventType, parser.prettyJson(json));
        }
    }
}
