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
import org.hyperledger.aries.api.out_of_band.OOBRecord;
import org.hyperledger.aries.api.present_proof.PresentationExchangeRecord;
import org.hyperledger.aries.api.present_proof_v2.V20PresExRecord;
import org.hyperledger.aries.api.revocation.RevocationEvent;
import org.hyperledger.aries.api.revocation.RevocationNotificationEvent;
import org.hyperledger.aries.api.revocation.RevocationNotificationEventV2;
import org.hyperledger.aries.api.trustping.PingEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * Reactive event handler
 */
@Slf4j
public class ReactiveEventHandler implements IEventHandler {

    protected static final int DEFAULT_BUFFER_SIZE = 100;

    private final EventParser parser = new EventParser();

    // Sinks
    // replay().limit(100)
    // multicast().onBackpressureBuffer(100, false);

    private final Sinks.Many<ConnectionRecord> connectionSink;
    private final Sinks.Many<PresentationExchangeRecord> presExSink;
    private final Sinks.Many<V20PresExRecord> presExV2Sink;
    private final Sinks.Many<V1CredentialExchange> credExSink;
    private final Sinks.Many<V20CredExRecord> credExV2Sink;
    private final Sinks.Many<V2IssueIndyCredentialEvent> credIssueIndySink;
    private final Sinks.Many<V2IssueLDCredentialEvent> credIssueLDSink;
    private final Sinks.Many<BasicMessage> basicMassageSink;
    private final Sinks.Many<PingEvent> pingEventSink;
    private final Sinks.Many<RevocationEvent> issuerRevocationEventSink;
    private final Sinks.Many<EndorseTransactionRecord> endorseTrxSink;
    private final Sinks.Many<ProblemReport> problemReportSink;
    private final Sinks.Many<DiscoverFeatureEvent> discoverFeatureSink;
    private final Sinks.Many<RevocationNotificationEvent> revocationNotificationSink;
    private final Sinks.Many<RevocationNotificationEventV2> revocationNotificationSinkV2;
    private final Sinks.Many<OOBRecord> oobRecordSink;

    public ReactiveEventHandler() {
        this(DEFAULT_BUFFER_SIZE);
    }

    public ReactiveEventHandler(int bufferSize) {
        this.connectionSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.presExSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.presExV2Sink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.credExSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.credExV2Sink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.credIssueIndySink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.credIssueLDSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.basicMassageSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.pingEventSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.issuerRevocationEventSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.endorseTrxSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.problemReportSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.discoverFeatureSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.revocationNotificationSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.revocationNotificationSinkV2 = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
        this.oobRecordSink = Sinks.many().multicast().onBackpressureBuffer(bufferSize, false);
    }

    public void handleEvent(String topic, String payload) {
        handleEvent(null, topic, payload);
    }

    public void handleEvent(String walletId, String topic, String payload) {

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
                    case REVOCATION_NOTIFICATION_V2:
                        parser.parseValueSave(payload, RevocationNotificationEventV2.class, revocationNotificationSinkV2::tryEmitNext);
                        break;
                    case OUT_OF_BAND:
                        parser.parseValueSave(payload, OOBRecord.class, oobRecordSink::tryEmitNext);
                        break;
                    default:
                        break;
                }
            } catch (Throwable e) {
                log.error("Error in reactive event handler:", e);
            }
        });
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

    public Flux<RevocationNotificationEventV2> revocationNotificationV2() {
        return revocationNotificationSinkV2.asFlux();
    }

    public Flux<OOBRecord> outOfBand() {
        return oobRecordSink.asFlux();
    }
}
