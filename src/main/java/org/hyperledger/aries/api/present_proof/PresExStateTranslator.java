/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

public interface PresExStateTranslator {

    PresentationExchangeState getState();

    PresentationExchangeRole getRole();

    PresentationExchangeInitiator getInitiator();

    Boolean getVerified();

    default boolean isVerified() {
        return getVerified() != null && getVerified();
    }

    default boolean roleIsProver() {
        return PresentationExchangeRole.PROVER.equals(getRole());
    }

    default boolean roleIsVerifier() {
        return PresentationExchangeRole.VERIFIER.equals(getRole());
    }

    default boolean roleIsProverAndRequestReceived() {
        return roleIsProver() && PresentationExchangeState.REQUEST_RECEIVED.equals(getState());
    }

    default boolean roleIsProverAndPresentationSent() {
        return roleIsProver() && PresentationExchangeState.PRESENTATIONS_SENT.equals(getState());
    }

    default boolean roleIsProverAndProposalSent() {
        return roleIsProver() && stateIsProposalSent();
    }

    default boolean roleIsProverAndPresentationAcked() {
        return roleIsProver() && PresentationExchangeState.PRESENTATION_ACKED.equals(getState());
    }

    default boolean roleIsVerifierAndRequestSent() {
        return roleIsVerifier() && PresentationExchangeState.REQUEST_SENT.equals(getState());
    }

    default boolean roleIsVerifierAndVerified() {
        return roleIsVerifier() && PresentationExchangeState.VERIFIED.equals(getState());
    }

    default boolean stateIsProposalSent() {
        return PresentationExchangeState.PROPOSAL_SENT.equals(getState());
    }

    default boolean initiatorIsSelf() {
        return PresentationExchangeInitiator.SELF.equals(getInitiator());
    }

    default boolean initiatorIsExternal() {
        return PresentationExchangeInitiator.EXTERNAL.equals(getInitiator());
    }
}
