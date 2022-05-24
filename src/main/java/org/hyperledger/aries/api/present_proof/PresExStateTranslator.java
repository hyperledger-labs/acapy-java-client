/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

public interface PresExStateTranslator {

    PresentationExchangeState getState();

    PresentationExchangeRole getRole();

    // Roles

    default boolean roleIsProver() {
        return PresentationExchangeRole.PROVER.equals(getRole());
    }

    default boolean roleIsVerifier() {
        return PresentationExchangeRole.VERIFIER.equals(getRole());
    }

    // Verifier States

    default boolean stateIsRequestSent() {
        return PresentationExchangeState.REQUEST_SENT.equals(getState());
    }

    default boolean stateIsProposalReceived() {
        return PresentationExchangeState.PROPOSAL_RECEIVED.equals(getState());
    }

    default boolean stateIsPresentationReceived() {
        return PresentationExchangeState.PRESENTATION_RECEIVED.equals(getState());
    }

    // Prover States

    default boolean stateIsRequestReceived() {
        return PresentationExchangeState.REQUEST_RECEIVED.equals(getState());
    }
    default boolean stateIsProposalSent() {
        return PresentationExchangeState.PROPOSAL_SENT.equals(getState());
    }

    default boolean stateIsPresentationSent() {
        return PresentationExchangeState.PRESENTATIONS_SENT.equals(getState());
    }

    // V1 States

    default boolean stateIsPresentationAcked() {
        return PresentationExchangeState.PRESENTATION_ACKED.equals(getState());
    }

    default boolean stateIsVerified() {
        return PresentationExchangeState.VERIFIED.equals(getState());
    }

    // General States

    default boolean stateIsAbandoned() {
        return PresentationExchangeState.ABANDONED.equals(getState());
    }

    default boolean stateIsDone() {
        return PresentationExchangeState.DONE.equals(getState());
    }

    default boolean roleIsProverAndRequestReceived() {
        return roleIsProver() && stateIsRequestReceived();
    }

    default boolean roleIsProverAndPresentationSent() {
        return roleIsProver() && stateIsPresentationSent();
    }

    default boolean roleIsProverAndProposalSent() {
        return roleIsProver() && stateIsProposalSent();
    }

    default boolean roleIsProverAndPresentationAcked() {
        return roleIsProver() && stateIsPresentationAcked();
    }

    // v1 or v2
    default boolean roleIsProverAndStateIsPresentationAckedOrDone() {
        return roleIsProver() && (stateIsPresentationAcked() || stateIsDone());
    }

    default boolean roleIsVerifierAndRequestSent() {
        return roleIsVerifier() && stateIsRequestSent();
    }

    // v1 or v2
    default boolean roleIsVerifierAndStateIsVerifiedOrDone() {
        return roleIsVerifier() && (stateIsVerified() || stateIsDone());
    }

    default boolean roleIsVerifierAndVerified() {
        return roleIsVerifier() && stateIsVerified();
    }

    default boolean roleIsVerifierAndDone() {
        return roleIsVerifier() && stateIsDone();
    }
}
