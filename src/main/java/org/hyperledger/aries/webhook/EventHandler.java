/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.acy_py.generated.model.IssuerRevRegRecord;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.discover_features.DiscoverFeatureEvent;
import org.hyperledger.aries.api.endorser.EndorseTransactionRecord;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.api.issue_credential_v2.V20CredExRecord;
import org.hyperledger.aries.api.issue_credential_v2.V2IssueIndyCredentialEvent;
import org.hyperledger.aries.api.issue_credential_v2.V2IssueLDCredentialEvent;
import org.hyperledger.aries.api.message.BasicMessage;
import org.hyperledger.aries.api.message.ProblemReport;
import org.hyperledger.aries.api.out_of_band.OOBRecord;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof_v2.V20PresExRecord;
import org.hyperledger.aries.api.revocation.RevocationEvent;
import org.hyperledger.aries.api.revocation.RevocationNotificationEvent;
import org.hyperledger.aries.api.revocation.RevocationNotificationEventV2;
import org.hyperledger.aries.api.settings.Settings;
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

        EventType.fromTopic(topic).ifPresent(t -> {
            try {
                switch (t) {
                    case CONNECTIONS:
                        parser.parseValueSave(payload, ConnectionRecord.class, this::handleConnection);
                        break;
                    case PRESENT_PROOF:
                        parser.parsePresentProof(payload).ifPresent(this::handleProof);
                        break;
                    case PRESENT_PROOF_V2:
                        parser.parseValueSave(payload, V20PresExRecord.class, this::handleProofV2);
                        break;
                    case ISSUE_CREDENTIAL:
                        parser.parseValueSave(payload, V1CredentialExchange.class, this::handleCredential);
                        break;
                    case ISSUE_CREDENTIAL_V2:
                        parser.parseValueSave(payload, V20CredExRecord.class, this::handleCredentialV2);
                        break;
                    case ISSUE_CREDENTIAL_V2_INDY:
                        parser.parseValueSave(payload, V2IssueIndyCredentialEvent.class, this::handleIssueCredentialV2Indy);
                        break;
                    case ISSUE_CREDENTIAL_V2_LD_PROOF:
                        parser.parseValueSave(payload, V2IssueLDCredentialEvent.class, this::handleIssueCredentialV2LD);
                        break;
                    case BASIC_MESSAGES:
                        parser.parseValueSave(payload, BasicMessage.class, this::handleBasicMessage);
                        break;
                    case PING:
                        parser.parseValueSave(payload, PingEvent.class, this::handlePing);
                        break;
                    case ISSUER_CRED_REV:
                        parser.parseValueSave(payload, RevocationEvent.class, this::handleRevocation);
                        break;
                    case ENDORSE_TRANSACTION:
                        parser.parseValueSave(payload, EndorseTransactionRecord.class, this::handleEndorseTransaction);
                        break;
                    case PROBLEM_REPORT:
                        parser.parseValueSave(payload, ProblemReport.class, this::handleProblemReport);
                        break;
                    case DISCOVER_FEATURE:
                        parser.parseValueSave(payload, DiscoverFeatureEvent.class, this::handleDiscoverFeature);
                        break;
                    case REVOCATION_NOTIFICATION:
                        parser.parseValueSave(payload, RevocationNotificationEvent.class, this::handleRevocationNotification);
                        break;
                    case REVOCATION_NOTIFICATION_V2:
                        parser.parseValueSave(payload, RevocationNotificationEventV2.class, this::handleRevocationNotificationV2);
                        break;
                    case REVOCATION_REGISTRY:
                        parser.parseValueSave(payload, IssuerRevRegRecord.class, this::handleRevocationRegistry);
                        break;
                    case SETTINGS:
                        parser.parseValueSave(payload, Settings.class, this::handleSettings);
                        break;
                    case OUT_OF_BAND:
                        parser.parseValueSave(payload, OOBRecord.class, this::handleOutOfBand);
                        break;
                    default:
                        break;
                }
            } catch (Throwable e) {
                log.error("Error in webhook event handler:", e);
            }
        });
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

    public void handleRevocationNotificationV2(RevocationNotificationEventV2 revocationNotificationV2) {
        log.debug(LOG_MSG_SINGLE, EventType.REVOCATION_NOTIFICATION_V2, revocationNotificationV2);
    }

    public void handleRevocationRegistry(IssuerRevRegRecord revocationRegistry) {
        log.debug(LOG_MSG_SINGLE, EventType.REVOCATION_REGISTRY, revocationRegistry);
    }

    public void handleEndorseTransaction(EndorseTransactionRecord transaction) {
        log.debug(LOG_MSG_SINGLE, EventType.ENDORSE_TRANSACTION, transaction);
    }

    public void handleProblemReport(ProblemReport report) {
        log.debug(LOG_MSG_SINGLE, EventType.PROBLEM_REPORT, report);
    }

    public void handleSettings(Settings settings) {
        log.debug(LOG_MSG_SINGLE, EventType.SETTINGS, settings);
    }

    public void handleOutOfBand(OOBRecord oob) {
        log.debug(LOG_MSG_SINGLE, EventType.OUT_OF_BAND, oob);
    }

    public void handleRaw(String eventType, String json) {
        if (log.isTraceEnabled()) {
            log.trace(LOG_MSG_SINGLE, eventType, parser.prettyJson(json));
        }
    }

    @NoArgsConstructor
    public static final class DefaultEventHandler extends EventHandler {
        //
    }
}
