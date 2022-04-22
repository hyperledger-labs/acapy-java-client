/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.webhook.reactive;

import lombok.Builder;
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
import org.hyperledger.aries.webhook.EventParser;
import org.hyperledger.aries.webhook.EventType;
import org.hyperledger.aries.webhook.IEventHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class ReactiveEventHandler implements IEventHandler {

    private static final int BUFFER_SIZE = 100;

    private final String walletId;

    private final EventParser parser = new EventParser();

    // Sinks
    // replay().limit(100)
    // multicast().onBackpressureBuffer(100, false);

    private final Sinks.Many<ConnectionRecord> connectionSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<PresentationExchangeRecord> presExSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<V20PresExRecord> presExV2Sink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<V1CredentialExchange> credExSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<V20CredExRecord> credExV2Sink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<V2IssueIndyCredentialEvent> credIssueIndySink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<V2IssueLDCredentialEvent> credIssueLDSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<BasicMessage> basicMassageSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<PingEvent> pingEventSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<RevocationEvent> issuerRevocationEventSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<EndorseTransactionRecord> endorseTrxSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<ProblemReport> problemReportSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<DiscoverFeatureEvent> discoverFeatureSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);
    private final Sinks.Many<RevocationNotificationEvent> revocationNotificationSink = Sinks.many().multicast().onBackpressureBuffer(BUFFER_SIZE, false);

    /**
     * Reactive event handler
     * @param walletId Optional: If set only forwards events for this walletId, all otherwise
     */
    @Builder
    public ReactiveEventHandler(String walletId) {
        this.walletId = walletId;
    }

    public void handleEvent(String topic, String payload) {
        handleEvent(null, topic, payload);
    }

    public void handleEvent(String walletId, String topic, String payload) {

        if (isOtherWalletId(walletId)) {
            return;
        }

        EventType.fromTopic(topic).ifPresent(t -> {
            try {
                switch (t) {
                    case CONNECTIONS:
                        parser.parseValueSave(payload, ConnectionRecord.class, connectionSink::tryEmitNext);
                        break;
                    case PRESENT_PROOF:
                        parser.parsePresentProof(payload).ifPresent(presExSink::tryEmitNext);
                        break;
                    case PRESENT_PROOF_V2:
                        parser.parseValueSave(payload, V20PresExRecord.class, presExV2Sink::tryEmitNext);
                        break;
                    case ISSUE_CREDENTIAL:
                        parser.parseValueSave(payload, V1CredentialExchange.class, credExSink::tryEmitNext);
                        break;
                    case ISSUE_CREDENTIAL_V2:
                        parser.parseValueSave(payload, V20CredExRecord.class, credExV2Sink::tryEmitNext);
                        break;
                    case ISSUE_CREDENTIAL_V2_INDY:
                        parser.parseValueSave(payload, V2IssueIndyCredentialEvent.class, credIssueIndySink::tryEmitNext);
                        break;
                    case ISSUE_CREDENTIAL_V2_LD_PROOF:
                        parser.parseValueSave(payload, V2IssueLDCredentialEvent.class, credIssueLDSink::tryEmitNext);
                        break;
                    case BASIC_MESSAGES:
                        parser.parseValueSave(payload, BasicMessage.class, basicMassageSink::tryEmitNext);
                        break;
                    case PING:
                        parser.parseValueSave(payload, PingEvent.class, pingEventSink::tryEmitNext);
                        break;
                    case ISSUER_CRED_REV:
                        parser.parseValueSave(payload, RevocationEvent.class, issuerRevocationEventSink::tryEmitNext);
                        break;
                    case ENDORSE_TRANSACTION:
                        parser.parseValueSave(payload, EndorseTransactionRecord.class, endorseTrxSink::tryEmitNext);
                        break;
                    case PROBLEM_REPORT:
                        parser.parseValueSave(payload, ProblemReport.class, problemReportSink::tryEmitNext);
                        break;
                    case DISCOVER_FEATURE:
                        parser.parseValueSave(payload, DiscoverFeatureEvent.class, discoverFeatureSink::tryEmitNext);
                        break;
                    case REVOCATION_NOTIFICATION:
                        parser.parseValueSave(payload, RevocationNotificationEvent.class, revocationNotificationSink::tryEmitNext);
                        break;
                    default:
                        break;
                }
            } catch (Throwable e) {
                log.error("Error in reactive event handler:", e);
            }
        });
    }

    private boolean isOtherWalletId(String walletId) {
        return this.walletId != null && !this.walletId.equals(walletId);
    }

    // Event Streams

    public Flux<ConnectionRecord> connection() {
        return connectionSink.asFlux();
    }

    public Flux<PresentationExchangeRecord> presentationEx() {
        return presExSink.asFlux();
    }

    public Flux<V20PresExRecord> presentationExV2() {
        return presExV2Sink.asFlux();
    }

    public Flux<V1CredentialExchange> credentialEx() {
        return credExSink.asFlux();
    }

    public Flux<V20CredExRecord> credentialExV2() {
        return credExV2Sink.asFlux();
    }

    public Flux<V2IssueIndyCredentialEvent> credentialIssueIndy() {
        return credIssueIndySink.asFlux();
    }

    public Flux<V2IssueLDCredentialEvent> credentialIssueLD() {
        return credIssueLDSink.asFlux();
    }

    public Flux<BasicMessage> basicMessage() {
        return basicMassageSink.asFlux();
    }

    public Flux<PingEvent> ping() {
        return pingEventSink.asFlux();
    }

    public Flux<RevocationEvent> issuerRevocation() {
        return issuerRevocationEventSink.asFlux();
    }

    public Flux<EndorseTransactionRecord> endorseTrx() {
        return endorseTrxSink.asFlux();
    }

    public Flux<ProblemReport> problemReport() {
        return problemReportSink.asFlux();
    }

    public Flux<DiscoverFeatureEvent> discoverFeature() {
        return discoverFeatureSink.asFlux();
    }

    public Flux<RevocationNotificationEvent> revocationNotification() {
        return revocationNotificationSink.asFlux();
    }
}
