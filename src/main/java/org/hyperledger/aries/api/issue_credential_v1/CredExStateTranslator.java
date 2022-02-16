/*
 * Copyright (c) 2020-2022 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

public interface CredExStateTranslator {

    CredentialExchangeState getState();

    CredentialExchangeRole getRole();

    default boolean stateIsProposalSent() {
        return CredentialExchangeState.PROPOSAL_SENT.equals(getState());
    }

    default boolean stateIsProposalReceived() {
        return CredentialExchangeState.PROPOSAL_RECEIVED.equals(getState());
    }

    default boolean stateIsOfferSent() {
        return CredentialExchangeState.OFFER_SENT.equals(getState());
    }

    default boolean stateIsOfferReceived() {
        return CredentialExchangeState.OFFER_RECEIVED.equals(getState());
    }

    default boolean stateIsRequestSent() {
        return CredentialExchangeState.REQUEST_SENT.equals(getState());
    }

    default boolean stateIsRequestReceived() {
        return CredentialExchangeState.REQUEST_RECEIVED.equals(getState());
    }

    default boolean stateIsCredentialIssued() {
        return CredentialExchangeState.CREDENTIAL_ISSUED.equals(getState());
    }

    default boolean stateIsCredentialReceived() {
        return CredentialExchangeState.CREDENTIAL_RECEIVED.equals(getState());
    }

    default boolean stateIsCredentialAcked() {
        return CredentialExchangeState.CREDENTIAL_ACKED.equals(getState());
    }

    default boolean stateIsDone() {
        return CredentialExchangeState.DONE.equals(getState());
    }

    default boolean stateIsDeclined() {
        return CredentialExchangeState.DECLINED.equals(getState());
    }

    default boolean stateIsNotDeclined() {
        return !CredentialExchangeState.DECLINED.equals(getState());
    }

    default boolean stateIsProblem() {
        return CredentialExchangeState.PROBLEM.equals(getState());
    }

    default boolean stateIsRevoked() {
        return CredentialExchangeState.CREDENTIAL_REVOKED.equals(getState());
    }

    default boolean stateIsAbandoned() {
        return CredentialExchangeState.ABANDONED.equals(getState());
    }

    default boolean roleIsIssuer() {
        return CredentialExchangeRole.ISSUER.equals(getRole());
    }

    default boolean roleIsHolder() {
        return CredentialExchangeRole.HOLDER.equals(getRole());
    }
}
