/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook;

import org.hyperledger.aries.api.discover_features.DiscoverFeatureEvent;
import org.hyperledger.aries.api.issue_credential_v2.V20CredExRecord;
import org.hyperledger.aries.api.issue_credential_v2.V2IssueIndyCredentialEvent;
import org.hyperledger.aries.api.issue_credential_v2.V2IssueLDCredentialEvent;
import org.hyperledger.aries.api.message.ProblemReport;
import org.hyperledger.aries.api.connection.ConnectionRecord;
import org.hyperledger.aries.api.issue_credential_v1.V1CredentialExchange;
import org.hyperledger.aries.api.message.BasicMessage;
import org.hyperledger.aries.api.revocation.RevocationNotificationEvent;
import org.hyperledger.aries.api.trustping.PingEvent;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.api.present_proof_v2.V20PresExRecord;
import org.hyperledger.aries.api.revocation.RevocationEvent;
import org.hyperledger.aries.api.endorser.EndorseTransactionRecord;

@Slf4j
public abstract class EventHandler {

    private final EventParser parser = new EventParser();

    public void handleEvent(String topic, String walletId, String payload) {

        handleRaw(topic, walletId, payload);

        try {
            if ("connections".equals(topic)) {
                handleConnection(walletId, parser.parseValueSave(payload, ConnectionRecord.class).get());
            } else if ("present_proof".equals(topic)) {
                handleProof(walletId, parser.parsePresentProof(payload).get());
            } else if ("present_proof_v2_0".equals(topic)) {
                handleProofV2(walletId, parser.parseValueSave(payload, V20PresExRecord.class).get());
            } else if ("issue_credential".equals(topic)) {
                handleCredential(walletId, parser.parseValueSave(payload, V1CredentialExchange.class).get());
            } else if ("issue_credential_v2_0".equals(topic)) {
                handleCredentialV2(walletId, parser.parseValueSave(payload, V20CredExRecord.class).get());
            } else if ("issue_credential_v2_0_indy".equals(topic)) {
                handleIssueCredentialV2Indy(walletId, parser.parseValueSave(payload, V2IssueIndyCredentialEvent.class).get());
            } else if ("issue_credential_v2_0_ld_proof".equals(topic)) {
                handleIssueCredentialV2LD(walletId, parser.parseValueSave(payload, V2IssueLDCredentialEvent.class).get());
            } else if ("basicmessages".equals(topic)) {
                handleBasicMessage(walletId, parser.parseValueSave(payload, BasicMessage.class).get());
            } else if ("ping".equals(topic)) {
                // WebSocket ping events may not have a payload
                if (payload != null)
                    handlePing(walletId, parser.parseValueSave(payload, PingEvent.class).get());
            } else if ("issuer_cred_rev".equals(topic)) {
                handleRevocation(walletId, parser.parseValueSave(payload, RevocationEvent.class).get());
            } else if ("endorse_transaction".equals(topic)) {
                handleEndorseTransaction(walletId, parser.parseValueSave(payload, EndorseTransactionRecord.class).get());
            } else if ("problem_report".equals(topic)) {
                handleProblemReport(walletId, parser.parseValueSave(payload, ProblemReport.class).get());
            } else if ("discover_feature".equals(topic)) {
                handleDiscoverFeature(walletId, parser.parseValueSave(payload, DiscoverFeatureEvent.class).get());
            } else if ("revocation-notification".equals(topic)) {
                handleRevocationNotification(walletId, parser.parseValueSave(payload, RevocationNotificationEvent.class).get());
            }
        } catch (Throwable e) {
            log.error("Error in webhook event handler:", e);
        }
    }

    public void handleConnection(String walletId, ConnectionRecord connection) throws Exception {
        log.debug("Connection Event: [{}] {}", walletId, connection);
        handleConnection(connection);
    }
    
    public void handleProof(String walletId, PresentationExchangeRecord proof) throws Exception {
        log.debug("Present Proof Event: [{}] {}", walletId, proof);
        handleProof(proof);
    }

    public void handleProofV2(String walletId, V20PresExRecord proof) throws Exception {
        log.debug("Present Proof V2 Event: [{}] {}", walletId, proof);
        handleProofV2(proof);
    }

    public void handleCredential(String walletId, V1CredentialExchange credential) throws Exception {
        log.debug("Issue Credential Event: [{}] {}", walletId, credential);
        handleCredential(credential);
    }

    public void handleCredentialV2(String walletId, V20CredExRecord credential) throws Exception {
        log.debug("Issue Credential V2 Event: [{}] {}", walletId, credential);
        handleCredentialV2(credential);
    }

    public void handleDiscoverFeature(String walletId, DiscoverFeatureEvent discoverFeature) throws Exception {
        log.debug("Discover Feature Event: [{}] {}", walletId, discoverFeature);
        handleDiscoverFeature(discoverFeature);
    }

    public void handleIssueCredentialV2Indy(String walletId, V2IssueIndyCredentialEvent credentialInfo) throws Exception {
        log.debug("Issue Credential V2 Indy Event: [{}] {}", walletId, credentialInfo);
        handleIssueCredentialV2Indy(credentialInfo);
    }

    public void handleIssueCredentialV2LD(String walletId, V2IssueLDCredentialEvent credentialInfo) throws Exception {
        log.debug("Issue LD Credential V2 Event: [{}] {}", walletId, credentialInfo);
        handleIssueCredentialV2LD(credentialInfo);
    }

    public void handleBasicMessage(String walletId, BasicMessage message) throws Exception {
        log.debug("Basic Message: [{}] {}", walletId, message);
        handleBasicMessage(message);
    }

    public void handlePing(String walletId, PingEvent ping) throws Exception {
        log.debug("Ping: [{}] {}", walletId, ping);
        handlePing(ping);
    }

    public void handleRevocation(String walletId, RevocationEvent revocation) throws Exception {
        log.debug("Revocation: [{}] {}", walletId, revocation);
        handleRevocation(revocation);
    }

    public void handleRevocationNotification(String walletId, RevocationNotificationEvent revocationNotification) throws Exception {
        log.debug("Revocation Notification: [{}] {}", walletId, revocationNotification);
        handleRevocationNotification(revocationNotification);
    }

    public void handleEndorseTransaction(String walletId, EndorseTransactionRecord transaction) throws Exception {
        log.debug("Endorse Transaction: [{}] {}", walletId, transaction);
        handleEndorseTransaction(transaction);
    }

    public void handleProblemReport(String walletId, ProblemReport report) throws Exception {
        log.debug("Problem Report: [{}] {}", walletId, report);
        handleProblemReport(report);
    }

    @Deprecated
    public void handleConnection(ConnectionRecord connection) throws Exception {
    }
    
    @Deprecated
    public void handleProof(PresentationExchangeRecord proof) throws Exception {
    }

    @Deprecated
    public void handleProofV2(V20PresExRecord proof) throws Exception {
    }

    @Deprecated
    public void handleCredential(V1CredentialExchange credential) throws Exception {
    }

    @Deprecated
    public void handleCredentialV2(V20CredExRecord v20Credential) throws Exception {
    }

    @Deprecated
    public void handleDiscoverFeature(DiscoverFeatureEvent discoverFeature) throws Exception {
    }

    @Deprecated
    public void handleIssueCredentialV2Indy(V2IssueIndyCredentialEvent credentialInfo) throws Exception {
    }

    @Deprecated
    public void handleIssueCredentialV2LD(V2IssueLDCredentialEvent credentialInfo) throws Exception {
    }

    @Deprecated
    public void handleBasicMessage(BasicMessage message) throws Exception {
    }

    @Deprecated
    public void handlePing(PingEvent ping) throws Exception {
    }

    @Deprecated
    public void handleRevocation(RevocationEvent revocation) throws Exception {
    }

    @Deprecated
    public void handleRevocationNotification(RevocationNotificationEvent revocationNotification) throws Exception {
    }

    @Deprecated
    public void handleEndorseTransaction(EndorseTransactionRecord transaction) throws Exception {
    }

    @Deprecated
    public void handleProblemReport(ProblemReport report) throws Exception {
    }

    public void handleRaw(String topic, String walletId, String payload) {
        if (log.isTraceEnabled()) {
            log.trace("Received event: {}, wallet: {}, payload:\n {}", topic, walletId, parser.prettyJson(payload));
        }
    }
}
