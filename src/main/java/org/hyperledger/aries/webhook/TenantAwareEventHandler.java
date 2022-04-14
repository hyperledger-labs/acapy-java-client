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
import org.hyperledger.aries.api.settings.Settings;
import org.hyperledger.aries.api.trustping.PingEvent;

/**
 * Event handler for multi tenant wallets. E.g. when running with --multitenant flag
 */
@Slf4j
public abstract class TenantAwareEventHandler implements IEventHandler {

    private static final String LOG_MSG_MULTI = "walletId: {}, topic: {}, payload: {}";

    private final EventParser parser = new EventParser();

    public void handleEvent(String topic, String payload) {
        // TODO should this be supported here?
        // either do
        // log.warn("Multi tenant event handler is used for single tenant events");
        // or
        // throw new IllegalStateException()
        handleEvent(null, topic, payload);
    }

    public void handleEvent(String walletId, String topic, String payload) {

        handleRaw(walletId, topic, payload);

        try {
            if (EventType.CONNECTIONS.valueEquals(topic)) {
                parser.parseValueSave(payload, ConnectionRecord.class).ifPresent(v -> handleConnection(walletId, v));
            } else if (EventType.PRESENT_PROOF.valueEquals(topic)) {
                parser.parsePresentProof(payload).ifPresent(v -> handleProof(walletId, v));
            } else if (EventType.PRESENT_PROOF_V2.valueEquals(topic)) {
                parser.parseValueSave(payload, V20PresExRecord.class).ifPresent(v -> handleProofV2(walletId, v));
            } else if (EventType.ISSUE_CREDENTIAL.valueEquals(topic)) {
                parser.parseValueSave(payload, V1CredentialExchange.class).ifPresent(v -> handleCredential(walletId, v));
            } else if (EventType.ISSUE_CREDENTIAL_V2.valueEquals(topic)) {
                parser.parseValueSave(payload, V20CredExRecord.class).ifPresent(v -> handleCredentialV2(walletId, v));
            } else if (EventType.ISSUE_CREDENTIAL_V2_INDY.valueEquals(topic)) {
                parser.parseValueSave(payload, V2IssueIndyCredentialEvent.class).ifPresent(v -> handleIssueCredentialV2Indy(walletId, v));
            } else if (EventType.ISSUE_CREDENTIAL_V2_LD_PROOF.valueEquals(topic)) {
                parser.parseValueSave(payload, V2IssueLDCredentialEvent.class).ifPresent(v -> handleIssueCredentialV2LD(walletId, v));
            } else if (EventType.BASIC_MESSAGES.valueEquals(topic)) {
                parser.parseValueSave(payload, BasicMessage.class).ifPresent(v -> handleBasicMessage(walletId, v));
            } else if (EventType.PING.valueEquals(topic)) {
                parser.parseValueSave(payload, PingEvent.class).ifPresent(v -> handlePing(walletId, v));
            } else if (EventType.ISSUER_CRED_REV.valueEquals(topic)) {
                parser.parseValueSave(payload, RevocationEvent.class).ifPresent(v -> handleRevocation(walletId, v));
            } else if (EventType.ENDORSE_TRANSACTION.valueEquals(topic)) {
                parser.parseValueSave(payload, EndorseTransactionRecord.class).ifPresent(v -> handleEndorseTransaction(walletId, v));
            } else if (EventType.PROBLEM_REPORT.valueEquals(topic)) {
                parser.parseValueSave(payload, ProblemReport.class).ifPresent(v -> handleProblemReport(walletId, v));
            } else if (EventType.DISCOVER_FEATURE.valueEquals(topic)) {
                parser.parseValueSave(payload, DiscoverFeatureEvent.class).ifPresent(v -> handleDiscoverFeature(walletId, v));
            } else if (EventType.REVOCATION_NOTIFICATION.valueEquals(topic)) {
                parser.parseValueSave(payload, RevocationNotificationEvent.class).ifPresent(v -> handleRevocationNotification(walletId, v));
            } else if (EventType.SETTINGS.valueEquals(topic)) {
                parser.parseValueSave(payload, Settings.class).ifPresent(v -> handleSettings(walletId, v));
            }
        } catch (Throwable e) {
            log.error("Error in webhook event handler:", e);
        }
    }

    public void handleConnection(String walletId, ConnectionRecord connection) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.CONNECTIONS, connection);
    }

    public void handleProof(String walletId, PresentationExchangeRecord proof) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PRESENT_PROOF, proof);
    }

    public void handleProofV2(String walletId, V20PresExRecord proof) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PRESENT_PROOF_V2, proof);
    }

    public void handleCredential(String walletId, V1CredentialExchange credential) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL, credential);
    }

    public void handleCredentialV2(String walletId, V20CredExRecord v20Credential) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL_V2, v20Credential);
    }

    public void handleDiscoverFeature(String walletId, DiscoverFeatureEvent discoverFeature) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.DISCOVER_FEATURE, discoverFeature);
    }

    public void handleIssueCredentialV2Indy(String walletId, V2IssueIndyCredentialEvent credentialInfo) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL_V2_INDY, credentialInfo);
    }

    public void handleIssueCredentialV2LD(String walletId, V2IssueLDCredentialEvent credentialInfo) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL_V2_LD_PROOF, credentialInfo);
    }

    public void handleBasicMessage(String walletId, BasicMessage message) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.BASIC_MESSAGES, message);
    }

    public void handlePing(String walletId, PingEvent ping) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PING, ping);
    }

    public void handleRevocation(String walletId, RevocationEvent revocation) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUER_CRED_REV, revocation);
    }

    public void handleRevocationNotification(String walletId, RevocationNotificationEvent revocationNotification) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.REVOCATION_NOTIFICATION, revocationNotification);
    }

    public void handleEndorseTransaction(String walletId, EndorseTransactionRecord transaction) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ENDORSE_TRANSACTION, transaction);
    }

    public void handleProblemReport(String walletId, ProblemReport report) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PROBLEM_REPORT, report);
    }

    public void handleSettings(String walletId, Settings settings) {
        log.debug(LOG_MSG_MULTI, walletId, EventType.SETTINGS, settings);
    }
    
    public void handleRaw(String walletId, String eventType, String json) {
        if (log.isTraceEnabled()) {
            log.trace(LOG_MSG_MULTI, walletId, eventType, parser.prettyJson(json));
        }
    }
}
