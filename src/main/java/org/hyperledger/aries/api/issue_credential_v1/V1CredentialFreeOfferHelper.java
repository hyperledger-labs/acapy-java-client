/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.issue_credential_v1;

import com.google.gson.Gson;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.aries.AriesClient;
import org.hyperledger.aries.api.connection.CreateInvitationParams;
import org.hyperledger.aries.api.connection.CreateInvitationRequest;
import org.hyperledger.aries.api.connection.CreateInvitationResponse;
import org.hyperledger.aries.api.credentials.CredentialAttributes;
import org.hyperledger.aries.api.credentials.CredentialPreview;
import org.hyperledger.aries.api.present_proof.ProofRequestPresentation;
import org.hyperledger.aries.config.GsonConfig;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Slf4j
public class V1CredentialFreeOfferHelper {

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private final Gson gson = GsonConfig.defaultNoEscaping();

    private final AriesClient acaPy;

    public V1CredentialFreeOfferHelper(AriesClient acaPy) {
        super();
        this.acaPy = acaPy;
    }

    public V1CredentialFreeOffer buildFreeOffer(@NonNull String credentialDefinitionId, Map<String, String> document) {
        V1CredentialFreeOffer.V1CredentialFreeOfferBuilder result = V1CredentialFreeOffer.builder();
        try{
            // step 1 - create credential
            V1CredentialCreate create = V1CredentialCreate
                    .builder()
                    .credDefId(credentialDefinitionId)
                    .credentialProposal(new CredentialPreview(CredentialAttributes.fromMap(document)))
                    .build();
            V1CredentialExchange ex = acaPy.issueCredentialCreate(create).orElseThrow();
            result
                    .credentialExchangeId(ex.getCredentialExchangeId())
                    .threadId(ex.getThreadId())
                    .type(ex.getCredentialOfferDict().getType())
                    .credentialPreview(ex.getCredentialOfferDict().getCredentialPreview())
                    .offersAttach(ex.getCredentialOfferDict().getOffersAttach());

            // step 2 - create invitation
            CreateInvitationRequest invReq = CreateInvitationRequest.builder().build();
            CreateInvitationParams invitationParams = CreateInvitationParams
                    .builder()
                    .autoAccept(Boolean.TRUE)
                    .multiUse(Boolean.FALSE)
                    .build();
            CreateInvitationResponse invitation = acaPy
                    .connectionsCreateInvitation(invReq, invitationParams).orElseThrow();
            result
                    .connectionId(invitation.getConnectionId())
                    .service(ProofRequestPresentation.ServiceDecorator
                    .builder()
                    .recipientKeys(invitation.getInvitation().getRecipientKeys())
                    .routingKeys(invitation.getInvitation().getRoutingKeys())
                    .serviceEndpoint(invitation.getInvitation().getServiceEndpoint())
                    .build());
        } catch (IOException e) {
            log.error("aca-py is not available", e);
        }
        return result.build();
    }

    public String toBase64(@NonNull V1CredentialFreeOffer offer) {
        byte[] envelopeBase64 = Base64.getEncoder().encode(gson.toJson(offer).getBytes(UTF_8));
        return new String(envelopeBase64, UTF_8);
    }
}
