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

        EventType.fromTopic(topic).ifPresent(t -> {
            try {
                switch (t) {
                    case CONNECTIONS:
                        handleConnection(walletId, parser.parseValueSave(payload, ConnectionRecord.class).orElseThrow());
                        break;
                    case PRESENT_PROOF:
                        handleProof(walletId, parser.parsePresentProof(payload).orElseThrow());
                        break;
                    case PRESENT_PROOF_V2:
                        handleProofV2(walletId, parser.parseValueSave(payload, V20PresExRecord.class).orElseThrow());
                        break;
                    case ISSUE_CREDENTIAL:
                        handleCredential(walletId, parser.parseValueSave(payload, V1CredentialExchange.class).orElseThrow());
                        break;
                    case ISSUE_CREDENTIAL_V2:
                        handleCredentialV2(walletId, parser.parseValueSave(payload, V20CredExRecord.class).orElseThrow());
                        break;
                    case ISSUE_CREDENTIAL_V2_INDY:
                        handleIssueCredentialV2Indy(walletId, parser.parseValueSave(payload, V2IssueIndyCredentialEvent.class).orElseThrow());
                        break;
                    case ISSUE_CREDENTIAL_V2_LD_PROOF:
                        handleIssueCredentialV2LD(walletId, parser.parseValueSave(payload, V2IssueLDCredentialEvent.class).orElseThrow());
                        break;
                    case BASIC_MESSAGES:
                        handleBasicMessage(walletId, parser.parseValueSave(payload, BasicMessage.class).orElseThrow());
                        break;
                    case PING:
                        handlePing(walletId, parser.parseValueSave(payload, PingEvent.class).orElseThrow());
                        break;
                    case ISSUER_CRED_REV:
                        handleRevocation(walletId, parser.parseValueSave(payload, RevocationEvent.class).orElseThrow());
                        break;
                    case ENDORSE_TRANSACTION:
                        handleEndorseTransaction(walletId, parser.parseValueSave(payload, EndorseTransactionRecord.class).orElseThrow());
                        break;
                    case PROBLEM_REPORT:
                        handleProblemReport(walletId, parser.parseValueSave(payload, ProblemReport.class).orElseThrow());
                        break;
                    case DISCOVER_FEATURE:
                        handleDiscoverFeature(walletId, parser.parseValueSave(payload, DiscoverFeatureEvent.class).orElseThrow());
                        break;
                    case REVOCATION_NOTIFICATION:
                        handleRevocationNotification(walletId, parser.parseValueSave(payload, RevocationNotificationEvent.class).orElseThrow());
                        break;
                    case REVOCATION_NOTIFICATION_V2:
                        handleRevocationNotificationV2(walletId, parser.parseValueSave(payload, RevocationNotificationEventV2.class).orElseThrow());
                        break;
                    case REVOCATION_REGISTRY:
                        handleRevocationRegistry(walletId, parser.parseValueSave(payload, IssuerRevRegRecord.class).orElseThrow());
                        break;
                    case SETTINGS:
                        handleSettings(walletId, parser.parseValueSave(payload, Settings.class).orElseThrow());
                        break;
                    case OUT_OF_BAND:
                        handleOutOfBand(walletId, parser.parseValueSave(payload, OOBRecord.class).orElseThrow());
                        break;
                    default:
                        break;
                }
            } catch (Throwable e) {
                log.error("Error in webhook event handler:", e);
            }
        });
    }

    public void handleConnection(String walletId, ConnectionRecord connection) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.CONNECTIONS, connection);
    }

    public void handleProof(String walletId, PresentationExchangeRecord proof) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PRESENT_PROOF, proof);
    }

    public void handleProofV2(String walletId, V20PresExRecord proof) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PRESENT_PROOF_V2, proof);
    }

    public void handleCredential(String walletId, V1CredentialExchange credential) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL, credential);
    }

    public void handleCredentialV2(String walletId, V20CredExRecord v20Credential) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL_V2, v20Credential);
    }

    public void handleDiscoverFeature(String walletId, DiscoverFeatureEvent discoverFeature) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.DISCOVER_FEATURE, discoverFeature);
    }

    public void handleIssueCredentialV2Indy(String walletId, V2IssueIndyCredentialEvent credentialInfo) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL_V2_INDY, credentialInfo);
    }

    public void handleIssueCredentialV2LD(String walletId, V2IssueLDCredentialEvent credentialInfo) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUE_CREDENTIAL_V2_LD_PROOF, credentialInfo);
    }

    public void handleBasicMessage(String walletId, BasicMessage message) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.BASIC_MESSAGES, message);
    }

    public void handlePing(String walletId, PingEvent ping) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PING, ping);
    }

    public void handleRevocation(String walletId, RevocationEvent revocation) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ISSUER_CRED_REV, revocation);
    }

    public void handleRevocationNotification(String walletId, RevocationNotificationEvent revocationNotification) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.REVOCATION_NOTIFICATION, revocationNotification);
    }

    public void handleRevocationNotificationV2(String walletId, RevocationNotificationEventV2 revocationNotificationV2) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.REVOCATION_NOTIFICATION_V2, revocationNotificationV2);
    }

    public void handleRevocationRegistry(String walletId, IssuerRevRegRecord revocationRegistry) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.REVOCATION_REGISTRY, revocationRegistry);
    }

    public void handleEndorseTransaction(String walletId, EndorseTransactionRecord transaction) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.ENDORSE_TRANSACTION, transaction);
    }

    public void handleProblemReport(String walletId, ProblemReport report) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.PROBLEM_REPORT, report);
    }

    public void handleSettings(String walletId, Settings settings) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.SETTINGS, settings);
    }

    public void handleOutOfBand(String walletId, OOBRecord oob) throws Exception {
        log.debug(LOG_MSG_MULTI, walletId, EventType.OUT_OF_BAND, oob);
    }
    
    public void handleRaw(String walletId, String eventType, String json) {
        if (log.isTraceEnabled()) {
            log.trace(LOG_MSG_MULTI, walletId, eventType, parser.prettyJson(json));
        }
    }

    @NoArgsConstructor
    public static final class DefaultTenantAwareEventHandler extends TenantAwareEventHandler {
        //
    }
}
