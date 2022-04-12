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

    public void handleEvent(String eventType, String json) {

        handleRaw(eventType, json);

        try {
            if ("connections".equals(eventType)) {
                handleConnection(parser.parseValueSave(json, ConnectionRecord.class).get());
            } else if ("present_proof".equals(eventType)) {
                handleProof(parser.parsePresentProof(json).get());
            } else if ("present_proof_v2_0".equals(eventType)) {
                handleProofV2(parser.parseValueSave(json, V20PresExRecord.class).get());
            } else if ("issue_credential".equals(eventType)) {
                handleCredential(parser.parseValueSave(json, V1CredentialExchange.class).get());
            } else if ("issue_credential_v2_0".equals(eventType)) {
                handleCredentialV2(parser.parseValueSave(json, V20CredExRecord.class).get());
            } else if ("issue_credential_v2_0_indy".equals(eventType)) {
                handleIssueCredentialV2Indy(parser.parseValueSave(json, V2IssueIndyCredentialEvent.class).get());
            } else if ("issue_credential_v2_0_ld_proof".equals(eventType)) {
                handleIssueCredentialV2LD(parser.parseValueSave(json, V2IssueLDCredentialEvent.class).get());
            } else if ("basicmessages".equals(eventType)) {
                handleBasicMessage(parser.parseValueSave(json, BasicMessage.class).get());
            } else if ("ping".equals(eventType)) {
                // WebSocket ping events may not have a payload
                if (json != null)
                    handlePing(parser.parseValueSave(json, PingEvent.class).get());
            } else if ("issuer_cred_rev".equals(eventType)) {
                handleRevocation(parser.parseValueSave(json, RevocationEvent.class).get());
            } else if ("endorse_transaction".equals(eventType)) {
                handleEndorseTransaction(parser.parseValueSave(json, EndorseTransactionRecord.class).get());
            } else if ("problem_report".equals(eventType)) {
                handleProblemReport(parser.parseValueSave(json, ProblemReport.class).get());
            } else if ("discover_feature".equals(eventType)) {
                handleDiscoverFeature(parser.parseValueSave(json, DiscoverFeatureEvent.class).get());
            } else if ("revocation-notification".equals(eventType)) {
                handleRevocationNotification(parser.parseValueSave(json, RevocationNotificationEvent.class).get());
            }
        } catch (Throwable e) {
            log.error("Error in webhook event handler:", e);
        }
    }

    public void handleConnection(ConnectionRecord connection) throws Exception {
        log.debug("Connection Event: {}", connection);
    }
    
    public void handleProof(PresentationExchangeRecord proof) throws Exception {
        log.debug("Present Proof Event: {}", proof);
    }

    public void handleProofV2(V20PresExRecord proof) throws Exception {
        log.debug("Present Proof V2 Event: {}", proof);
    }

    public void handleCredential(V1CredentialExchange credential) throws Exception {
        log.debug("Issue Credential Event: {}", credential);
    }

    public void handleCredentialV2(V20CredExRecord v20Credential) throws Exception {
        log.debug("Issue Credential V2 Event: {}", v20Credential);
    }

    public void handleDiscoverFeature(DiscoverFeatureEvent discoverFeature) throws Exception {
        log.debug("Discover Feature Event: {}", discoverFeature);
    }

    public void handleIssueCredentialV2Indy(V2IssueIndyCredentialEvent credentialInfo) throws Exception {
        log.debug("Issue Credential V2 Indy Event: {}", credentialInfo);
    }

    public void handleIssueCredentialV2LD(V2IssueLDCredentialEvent credentialInfo) throws Exception {
        log.debug("Issue LD Credential V2 Event: {}", credentialInfo);
    }

    public void handleBasicMessage(BasicMessage message) throws Exception {
        log.debug("Basic Message: {}", message);
    }

    public void handlePing(PingEvent ping) throws Exception {
        log.debug("Ping: {}", ping);
    }

    public void handleRevocation(RevocationEvent revocation) throws Exception {
        log.debug("Revocation: {}", revocation);
    }

    public void handleRevocationNotification(RevocationNotificationEvent revocationNotification) throws Exception {
        log.debug("Revocation Notification: {}", revocationNotification);
    }

    public void handleEndorseTransaction(EndorseTransactionRecord transaction) throws Exception {
        log.debug("Endorse Transaction: {}", transaction);
    }

    public void handleProblemReport(ProblemReport report) throws Exception {
        log.debug("Problem Report: {}", report);
    }

    public void handleRaw(String eventType, String json) {
        if (log.isTraceEnabled()) {
            log.trace("Received event: {}, body:\n {}", eventType, parser.prettyJson(json));
        }
    }
}
