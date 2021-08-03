/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.acy_py.generated.model.*;
import org.hyperledger.aries.api.action_menu.PerformRequest;
import org.hyperledger.aries.api.action_menu.SendMenu;
import org.hyperledger.aries.api.connection.ConnectionStaticRequest;
import org.hyperledger.aries.api.connection.ConnectionStaticResult;
import org.hyperledger.aries.api.connection.CreateInvitationRequest;
import org.hyperledger.aries.api.connection.ReceiveInvitationRequest;
import org.hyperledger.aries.api.connection.*;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition.CredentialDefinitionRequest;
import org.hyperledger.aries.api.credential_definition.CredentialDefinition.CredentialDefinitionsCreated;
import org.hyperledger.aries.api.credential_definition.CredentialDefinitionFilter;
import org.hyperledger.aries.api.credentials.Credential;
import org.hyperledger.aries.api.credentials.CredentialFilter;
import org.hyperledger.aries.api.did_exchange.DIDXRequest;
import org.hyperledger.aries.api.did_exchange.*;
import org.hyperledger.aries.api.endorser.*;
import org.hyperledger.aries.api.exception.AriesException;
import org.hyperledger.aries.api.issue_credential_v1.*;
import org.hyperledger.aries.api.jsonld.SignRequest;
import org.hyperledger.aries.api.jsonld.VerifyRequest;
import org.hyperledger.aries.api.jsonld.VerifyResponse;
import org.hyperledger.aries.api.jsonld.*;
import org.hyperledger.aries.api.ledger.DidVerkeyResponse;
import org.hyperledger.aries.api.ledger.EndpointResponse;
import org.hyperledger.aries.api.ledger.TAAAccept;
import org.hyperledger.aries.api.ledger.TAAInfo;
import org.hyperledger.aries.api.message.PingRequest;
import org.hyperledger.aries.api.message.PingResponse;
import org.hyperledger.aries.api.multitenancy.CreateWalletRequest;
import org.hyperledger.aries.api.multitenancy.CreateWalletTokenRequest;
import org.hyperledger.aries.api.multitenancy.CreateWalletTokenResponse;
import org.hyperledger.aries.api.multitenancy.RemoveWalletRequest;
import org.hyperledger.aries.api.multitenancy.UpdateWalletRequest;
import org.hyperledger.aries.api.multitenancy.WalletRecord;
import org.hyperledger.aries.api.out_of_band.CreateInvitationFilter;
import org.hyperledger.aries.api.out_of_band.InvitationMessage;
import org.hyperledger.aries.api.out_of_band.ReceiveInvitationFilter;
import org.hyperledger.aries.api.present_proof.AdminAPIMessageTracing;
import org.hyperledger.aries.api.present_proof.PresentationRequest;
import org.hyperledger.aries.api.present_proof.*;
import org.hyperledger.aries.api.resolver.DIDDocument;
import org.hyperledger.aries.api.revocation.RevRegCreateRequest;
import org.hyperledger.aries.api.revocation.RevRegUpdateTailsFileUri;
import org.hyperledger.aries.api.revocation.RevRegsCreated;
import org.hyperledger.aries.api.revocation.RevokeRequest;
import org.hyperledger.aries.api.revocation.*;
import org.hyperledger.aries.api.schema.SchemaSendRequest;
import org.hyperledger.aries.api.schema.SchemaSendResponse;
import org.hyperledger.aries.api.schema.SchemaSendResponse.Schema;
import org.hyperledger.aries.api.schema.SchemasCreatedFilter;
import org.hyperledger.aries.api.server.AdminConfig;
import org.hyperledger.aries.api.server.AdminStatusLiveliness;
import org.hyperledger.aries.api.server.AdminStatusReadiness;
import org.hyperledger.aries.config.TimeUtil;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * ACA-PY client
 */
@Slf4j
@SuppressWarnings("unused")
public class AriesClient extends BaseClient {

    private final String url;
    private final String apiKey;
    private final String bearerToken;

    /**
     * @param url The aca-py admin api URL without a path e.g. protocol://host:[port]
     * @param apiKey The admin api api key
     * @param bearerToken the Bearer token used in the Authorization header when running in multi tenant mode
     * @param client {@link OkHttpClient} if null a default client is created
     */
    @Builder
    public AriesClient(@NonNull String url, @Nullable String apiKey,
                       @Nullable String bearerToken, @Nullable OkHttpClient client) {
        super(client);
        this.url = StringUtils.trim(url);
        this.apiKey = StringUtils.trimToEmpty(apiKey);
        this.bearerToken = StringUtils.trimToEmpty(bearerToken);
    }

    // ----------------------------------------------------
    // Action Menu - Menu interaction over connection
    // ----------------------------------------------------

    /**
     * Close the active menu associated with a connection
     * @param connectionId the connection id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void actionMenuClose(@NonNull String connectionId) throws IOException {
        Request req = buildPost(url + "/action-menu/" + connectionId + "/close", EMPTY_JSON);
        call(req);
    }

    /**
     * Fetch the active menu
     * @param connectionId the connection id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void actionMenuFetch(@NonNull String connectionId) throws IOException {
        Request req = buildPost(url + "/action-menu/" + connectionId + "/fetch", EMPTY_JSON);
        call(req);
    }

    /**
     * Perform an action associated with the active menu
     * @param connectionId the connection id
     * @param request {@link PerformRequest}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void actionMenuPerform(@NonNull String connectionId, @NonNull PerformRequest request) throws IOException {
        Request req = buildPost(url + "/action-menu/" + connectionId + "/perform", request);
        call(req);
    }

    /**
     * Request the active menu
     * @param connectionId the connection id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void actionMenuRequest(@NonNull String connectionId) throws IOException {
        Request req = buildPost(url + "/action-menu/" + connectionId + "/request", EMPTY_JSON);
        call(req);
    }

    /**
     * Send an action menu to a connection
     * @param connectionId the connection id
     * @param request {@link SendMenu}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void actionMenuSendMenu(@NonNull String connectionId, @NonNull SendMenu request) throws IOException {
        Request req = buildPost(url + "/action-menu/" + connectionId + "/send-menu", request);
        call(req);
    }

    // ----------------------------------------------------
    // Basic Message - Simple Messaging
    // ----------------------------------------------------

    /**
     * Send a basic message to a connection
     * @param connectionId the connection id
     * @param msg {@link SendMessage} the message
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void connectionsSendMessage(@NonNull String connectionId, @NonNull SendMessage msg) throws IOException {
        Request req = buildPost(url + "/connections/" + connectionId + "/send-message", msg);
        call(req);
    }

    // ----------------------------------------------------
    // Connection - Connection Management
    // ----------------------------------------------------

    /**
     * Query agent-to-agent connections
     * @return List of agent-to-agent connections
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<ConnectionRecord>> connections() throws IOException {
        return connections(null);
    }

    /**
     * Query agent-to-agent connections
     * @param filter {@link ConnectionFilter}
     * @return List of agent-to-agent connections
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<ConnectionRecord>> connections(@Nullable ConnectionFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/connections")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", CONNECTION_TYPE);
    }

    /**
     * Query agent-to-agent connections
     * @return only the connection IDs
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public List<String> connectionIds() throws IOException {
        return connectionIds(null);
    }

    /**
     * Query agent-to-agent connections
     * @param filter {@link ConnectionFilter}
     * @return only the connection IDs based on the filter criteria
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public List<String> connectionIds(@Nullable ConnectionFilter filter) throws IOException {
        List<String> result = new ArrayList<>();
        final Optional<List<ConnectionRecord>> c = connections(filter);
        if (c.isPresent()) {
            result = c.get().stream().map(ConnectionRecord::getConnectionId).collect(Collectors.toList());
        }
        return result;
    }

    /**
     * Create a new connection invitation
     * @return {@link CreateInvitationResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     * @deprecated see {@link #connectionsCreateInvitation(CreateInvitationRequest)}
     */
    @Deprecated
    public Optional<CreateInvitationResponse> connectionsCreateInvitation() throws IOException {
        Request req = buildPost(url + "/connections/create-invitation", EMPTY_JSON);
        return call(req, CreateInvitationResponse.class);
    }

    /**
     * Create a new connection invitation
     * @param request {@link CreateInvitationRequest}
     * @return {@link CreateInvitationResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     * @since 0.6.0
     */
    public Optional<CreateInvitationResponse> connectionsCreateInvitation(@NonNull CreateInvitationRequest request)
            throws IOException {
        return connectionsCreateInvitation(request, null);
    }

    /**
     * Create a new connection invitation
     * @param request {@link CreateInvitationRequest}
     * @param params {@link CreateInvitationParams}
     * @return {@link CreateInvitationResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     * @since 0.6.0
     */
    public Optional<CreateInvitationResponse> connectionsCreateInvitation(
            @NonNull CreateInvitationRequest request, @Nullable CreateInvitationParams params) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/connections/create-invitation")).newBuilder();
        if (params != null) {
            params.buildParams(b);
        }
        Request req = buildPost(b.build().toString(), request);
        return call(req, CreateInvitationResponse.class);
    }

    /**
     * Create a new static connection
     * @param request {@link ConnectionStaticRequest}
     * @return {@link ConnectionStaticResult}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionStaticResult> connectionsCreateStatic(@NonNull ConnectionStaticRequest request)
            throws IOException {
        Request req = buildPost(url + "/connections/create-static", request);
        return call(req, ConnectionStaticResult.class);
    }

    /**
     * Receive a new connection invitation
     * @param invite {@link ReceiveInvitationRequest}
     * @param filter optional: {@link ConnectionReceiveInvitationFilter}
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> connectionsReceiveInvitation(
            @NonNull ReceiveInvitationRequest invite, @Nullable ConnectionReceiveInvitationFilter filter)
            throws IOException{
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/connections/receive-invitation")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.build().toString(), invite);
        return call(req, ConnectionRecord.class);
    }

    /**
     * Fetch a single connection record
     * @param connectionId the connection id
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> connectionsGetById(@NonNull String connectionId) throws IOException {
        Request req = buildGet(url + "/connections/" + connectionId);
        return call(req, ConnectionRecord.class);
    }

    /**
     * Remove an existing connection record
     * @param connectionId the connection id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void connectionsRemove(@NonNull String connectionId) throws IOException {
        Request req = buildDelete(url + "/connections/" + connectionId);
        call(req);
    }

    /**
     * Accept a stored connection invitation
     * @param connectionId the connection id
     * @param filter optional {@link ConnectionAcceptInvitationFilter}
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> connectionsAcceptInvitation(@NonNull String connectionId,
        @Nullable ConnectionAcceptInvitationFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
            HttpUrl.parse(url + "/connections/" + connectionId + "/accept-invitation")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.toString(), EMPTY_JSON);
        return call(req, ConnectionRecord.class);
    }

    /**
     * Accept a stored connection request
     * @param connectionId the connection id
     * @param filter optional {@link ConnectionAcceptRequestFilter}
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> connectionsAcceptRequest(@NonNull String connectionId,
        @Nullable ConnectionAcceptRequestFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/connections/" + connectionId + "/accept-request")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.toString(), EMPTY_JSON);
        return call(req, ConnectionRecord.class);
    }

    /**
     * Fetch a connection remote endpoint
     * @param connectionId the connection id
     * @return {@link EndpointResult}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndpointResult> connectionsGetEndpoints(@NonNull String connectionId) throws IOException {
        Request req = buildGet(url + "/connections/" + connectionId + "/endpoints");
        return call(req, EndpointResult.class);
    }

    /**
     * Assign another connection as the inbound connection
     * @param connectionId the connection identifier
     * @param inboundConnectionId inbound connection identifier
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void connectionsEstablishInbound(
            @NonNull String connectionId, @NonNull String inboundConnectionId) throws IOException {
        Request req = buildPost(
                url + "/connections/" + connectionId + "/establish-inbound/" + inboundConnectionId, EMPTY_JSON);
        call(req);
    }

    /**
     * Fetch connection metadata
     * @param connectionId the connection id
     * @return {@link Map} metadata map
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<Map<String, String>> connectionsGetMetadata(@NonNull String connectionId) throws IOException {
        Request req = buildGet(url + "/connections/" + connectionId + "/metadata");
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", MAP_TYPE);
    }

    /**
     * Fetch connection metadata
     * @param connectionId the connection id
     * @param key Key to retrieve
     * @return single value string
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<String> connectionsGetMetadata(@NonNull String connectionId, @NonNull String key)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/connections/" + connectionId + "/metadata")).newBuilder();
        b.addQueryParameter("key", key);
        Request req = buildGet(b.toString());
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", String.class);
    }

    /**
     * Set connection metadata
     * @param connectionId the connection id
     * @param request {@link ConnectionSetMetaDataRequest}
     * @return {@link Map} metadata map
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<Map<String, String>> connectionsSetMetadata(@NonNull String connectionId,
        @NonNull ConnectionSetMetaDataRequest request) throws IOException {
        Request req = buildPost(url + "/connections/" + connectionId + "/metadata", request);
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", MAP_TYPE);
    }

    // ----------------------------------------------------
    // Credential Definition - Credential Definition Operations
    // ----------------------------------------------------

    /**
     * Sends a credential definition to the ledger
     * @param defReq {@link CredentialDefinitionRequest}
     * @return {@link CredentialDefinition.CredentialDefinitionResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<CredentialDefinition.CredentialDefinitionResponse> credentialDefinitionsCreate(
            @NonNull CredentialDefinitionRequest defReq) throws IOException {
        Request req = buildPost(url + "/credential-definitions", defReq);
        return call(req, CredentialDefinition.CredentialDefinitionResponse.class);
    }

    /**
     * Sends a credential definition to the ledger via an endorser
     * @since aca-py 0.7.0
     * @param defReq {@link CredentialDefinitionRequest}
     * @param endorserInfoFilter {@link EndorserInfoFilter}
     * @return {@link TxnOrCredentialDefinitionSendResult}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<TxnOrCredentialDefinitionSendResult> credentialDefinitionsCreate(
            @NonNull CredentialDefinitionRequest defReq, @NonNull EndorserInfoFilter endorserInfoFilter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl
                .parse(url + "/credential-definitions")).newBuilder();
        endorserInfoFilter.buildParams(b);
        Request req = buildPost(b.toString(), defReq);
        return call(req, TxnOrCredentialDefinitionSendResult.class);
    }

    /**
     * Search for matching credential definitions that originated from this agent
     * @param filter {@link CredentialDefinitionFilter}
     * @return {@link CredentialDefinitionsCreated}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<CredentialDefinitionsCreated> credentialDefinitionsCreated(
            @Nullable CredentialDefinitionFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/credential-definitions/created")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        return call(req , CredentialDefinitionsCreated.class);
    }

    /**
     * Gets a credential definition from the ledger
     * @param id credential definition id
     * @return {@link CredentialDefinition}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<CredentialDefinition> credentialDefinitionsGetById(@NonNull String id) throws IOException {
        Request req = buildGet(url + "/credential-definitions/" + id);
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "credential_definition", CredentialDefinition.class);
    }

    // ----------------------------------------------------
    // Credentials- Holder Credential Management
    // ----------------------------------------------------

    // TODO no model, create a couple of credentials with mime types and see what happens
    /**
     * Get attribute MIME types from wallet
     * @param credentialId credential id
     * @return ???
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<Object> credentialMimeTypes(@NonNull String credentialId)
            throws IOException {
        Request req = buildGet(url + "/credential/mime-types/" + credentialId);
        return call(req, Object.class);
    }

    /**
     * Query credential revocation status by id
     * @param credentialId credentialId
     * @return {@link Credential.CredentialRevokedResult}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<Credential.CredentialRevokedResult> credentialRevoked(@NonNull String credentialId)
            throws IOException {
        Request req = buildGet(url + "/credential/revoked/" + credentialId);
        return call(req, Credential.CredentialRevokedResult.class);
    }

    /**
     * Fetch a credential from wallet by id
     * @param credentialId credentialId
     * @return {@link Credential}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<Credential> credential(@NonNull String credentialId) throws IOException {
        Request req = buildGet(url + "/credential/" + credentialId);
        return call(req, Credential.class);
    }

    /**
     * Remove a credential from the wallet by id (credentialId)
     * @param credentialId credentialId
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void credentialRemove(@NonNull String credentialId) throws IOException {
        Request req = buildDelete(url + "/credential/" + credentialId);
        call(req);
    }

    /**
     * Fetch credentials from wallet
     * @return list of credentials {@link Credential}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<Credential>> credentials() throws IOException {
        return credentials(null);
    }

    /**
     * Fetch credentials from wallet
     * @param filter see {@link CredentialFilter} for prepared filters
     * @return Credentials that match the filter criteria
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<Credential>> credentials(@Nullable Predicate<Credential> filter) throws IOException {
        Optional<List<Credential>> result = Optional.empty();
        Request req = buildGet(url + "/credentials");
        final Optional<String> resp = raw(req);
        if (resp.isPresent()) {
            result = getWrapped(resp, "results", CREDENTIAL_TYPE);
            if (result.isPresent() && filter != null) {
                result = Optional.of(result.get().stream().filter(filter).collect(Collectors.toList()));
            }
        }
        return result;
    }

    /**
     * Fetch credentials ids from wallet
     * @return only the credential IDs
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public List<String> credentialIds() throws IOException {
        return credentialIds(null);
    }

    /**
     * Fetch credentials ids from wallet
     * @param filter see {@link CredentialFilter} for prepared filters
     * @return only the credential IDs based on the filter criteria
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public List<String> credentialIds(@Nullable Predicate<Credential> filter) throws IOException {
        List<String> result = new ArrayList<>();
        final Optional<List<Credential>> c = credentials(filter);
        if (c.isPresent()) {
            result = c.get().stream().map(Credential::getReferent).collect(Collectors.toList());
        }
        return result;
    }

    // ----------------------------------------------------
    // DID Exchange - Connection management via DID exchange
    // ----------------------------------------------------

    /**
     * Create request against public DID's implicit invitation
     * @since aca-py 0.7.0
     * @param filter {@link DidExchangeCreateRequestFilter}
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> didExchangeCreateRequest(@NonNull DidExchangeCreateRequestFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/didexchange/create-request")).newBuilder();
        filter.buildParams(b);
        Request req = buildPost(b.toString(), EMPTY_JSON);
        return call(req, ConnectionRecord.class);
    }

    /**
     * Receive request against public DID's implicit invitation
     * @since aca-py 0.7.0
     * @param request {@link DIDXRequest}
     * @param filter {@link DidExchangeReceiveRequestFilter}
     * @return {link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> didExchangeReceiveRequest(@NonNull DIDXRequest request,
        @Nullable DidExchangeReceiveRequestFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/didexchange/receive-request")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.toString(), request);
        return call(req, ConnectionRecord.class);
    }

    /**
     * Accept a stored connection invitation
     * @since aca-py 0.7.0
     * @param connectionId the connection id
     * @param filter {@link DidExchangeAcceptInvitationFilter}
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> didExchangeAcceptInvitation(@NonNull String connectionId,
        @Nullable DidExchangeAcceptInvitationFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/didexchange/" + connectionId + "/accept-invitation")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.toString(), EMPTY_JSON);
        return call(req, ConnectionRecord.class);
    }

    /**
     * Accept a stored connection request
     * @since aca-py 0.7.0
     * @param connectionId the connection id
     * @param filter {@link DidExchangeAcceptRequestFilter}
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> didExchangeAcceptRequest(@NonNull String connectionId,
        @Nullable DidExchangeAcceptRequestFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/didexchange/" + connectionId + "/accept-request")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.toString(), EMPTY_JSON);
        return call(req, ConnectionRecord.class);
    }

    // ----------------------------------------------------
    // Endorse Transaction - Endorse a transaction
    // ----------------------------------------------------

    /**
     * For Author to resend a particular transaction request
     * @since aca-py 0.7.0
     * @param trxId transaction id
     * @return {@link EndorseTransactionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorseTransactionRecord> endorseTransactionResend(@NonNull String trxId) throws IOException {
        Request req = buildPost(url + "/transaction/" + trxId + "/resend", EMPTY_JSON);
        return call(req, EndorseTransactionRecord.class);
    }

    /**
     * Query transactions
     * @since aca-py 0.7.0
     * @return list of {@link EndorseTransactionRecord}}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<EndorseTransactionRecord>> endorseGetTransactions() throws IOException {
        Request req = buildGet(url + "/transactions");
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", TRX_RECORD_TYPE);
    }

    /**
     * For author to send a transaction request
     * @since aca-py 0.7.0
     * @param expiresTime when the request should expire
     * @param filter {@link EndorseCreateRequestFilter}
     * @return {@link EndorseTransactionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorseTransactionRecord> endorseTransactionCreateRequest(
            @NonNull Instant expiresTime, @NonNull EndorseCreateRequestFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/transactions/create-request")).newBuilder();
        filter.buildParams(b);
        Request req = buildPost(b.toString(), EndorseCreateRequest
                .builder()
                .expiresTime(TimeUtil.currentTimeFormatted(expiresTime))
                .build());
        return call(req, EndorseTransactionRecord.class);
    }

    /**
     * Set Endorser Info
     * @since aca-py 0.7.0
     * @param connectionId the connection id
     * @param filter {@link SetEndorserInfoFilter}
     * @return {@link EndorserInfo}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorserInfo> endorseTransactionSetEndorserInfo(
            @NonNull String connectionId, @NonNull SetEndorserInfoFilter filter) throws IOException{
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/transactions/" + connectionId + "/set-endorser-info")).newBuilder();
        filter.buildParams(b);
        Request req = buildPost(b.toString(), EMPTY_JSON);
        return call(req, EndorserInfo.class);
    }

    /**
     * Set transaction jobs
     * @since aca-py 0.7.0
     * @param connectionId the connection id
     * @param filter {@link SetEndorserRoleFilter}
     * @return {@link TransactionJobs}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<TransactionJobs> endorseTransactionSetEndorserRole(
            @NonNull String connectionId, @NonNull SetEndorserRoleFilter filter) throws IOException{
        HttpUrl.Builder b = Objects.requireNonNull(
                HttpUrl.parse(url + "/transactions/" + connectionId + "/set-endorser-role")).newBuilder();
        filter.buildParams(b);
        Request req = buildPost(b.toString(), EMPTY_JSON);
        return call(req, TransactionJobs.class);
    }

    /**
     * Fetch single transaction record
     * @since aca-py 0.7.0
     * @param trxId the transaction id
     * @return {@link EndorseTransactionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorseTransactionRecord> endorseTransactionGetById(@NonNull String trxId) throws IOException {
        Request req = buildGet(url + "/transactions/" + trxId);
        return call(req, EndorseTransactionRecord.class);
    }

    /**
     * For Author to cancel a particular transaction request
     * @since aca-py 0.7.0
     * @param trxId transaction id
     * @return {@link EndorseTransactionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorseTransactionRecord> endorseTransactionCancel(@NonNull String trxId) throws IOException{
        Request req = buildPost(url + "/transactions/" + trxId + "/cancel", EMPTY_JSON);
        return call(req, EndorseTransactionRecord.class);
    }

    /**
     * For Endorser to endorse a particular transaction record
     * @since aca-py 0.7.0
     * @param trxId transaction id
     * @return {@link EndorseTransactionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorseTransactionRecord> endorseTransactionEndorse(@NonNull String trxId) throws IOException{
        Request req = buildPost(url + "/transactions/" + trxId + "/endorse", EMPTY_JSON);
        return call(req, EndorseTransactionRecord.class);
    }

    /**
     * For Endorser to refuse a particular transaction record
     * @since aca-py 0.7.0
     * @param trxId transaction id
     * @return {@link EndorseTransactionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorseTransactionRecord> endorseTransactionRefuse(@NonNull String trxId) throws IOException{
        Request req = buildPost(url + "/transactions/" + trxId + "/refuse", EMPTY_JSON);
        return call(req, EndorseTransactionRecord.class);
    }

    /**
     * For Author / Endorser to write an endorsed transaction to the ledger
     * @since aca-py 0.7.0
     * @param trxId transaction id
     * @return {@link EndorseTransactionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndorseTransactionRecord> endorseTransactionWrite(@NonNull String trxId) throws IOException{
        Request req = buildPost(url + "/transactions/" + trxId + "/write", EMPTY_JSON);
        return call(req, EndorseTransactionRecord.class);
    }

    // ----------------------------------------------------
    // Introduction - introduction of known parties
    // ----------------------------------------------------

    // TODO

    // ----------------------------------------------------
    // Issue Credential - Credential Issue v1.0
    // ----------------------------------------------------

    /**
     * Send holder a credential, automating the entire flow
     * @param request {@link V1CredentialCreate}
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialCreate(@NonNull V1CredentialCreate request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential/create", request);
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Fetch all credential exchange records
     * @param filter {@link IssueCredentialRecordsFilter}
     * @return list of {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<V1CredentialExchange>> issueCredentialRecords(IssueCredentialRecordsFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/issue-credential/records")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", ISSUE_CREDENTIAL_TYPE);
    }

    /**
     * Fetch a single credential exchange record
     * @param credentialExchangeId credential exchange identifier
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialRecordsGetById(@NonNull String credentialExchangeId)
            throws IOException {
        Request req = buildGet(url + "/issue-credential/records/" + credentialExchangeId);
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Remove an existing credential exchange record
     * @param credentialExchangeId the credential exchange id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void issueCredentialRecordsRemove(@NonNull String credentialExchangeId) throws IOException {
        Request req = buildDelete(url + "/issue-credential/records/" + credentialExchangeId);
        call(req);
    }

    /**
     * Send holder a credential
     * @param credentialExchangeId credential exchange identifier
     * @param request optional {@link V1CredentialIssueRequest}
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialRecordsIssue(
            @NonNull String credentialExchangeId, @Nullable V1CredentialIssueRequest request) throws IOException {
        Request req = buildPost(url + "/issue-credential/records/" + credentialExchangeId + "/issue",
                request != null ? request : EMPTY_JSON);
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Send a problem report for a credential exchange
     * @param credentialExchangeId credential exchange identifier
     * @param request {@link V1CredentialProblemReportRequest}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void issueCredentialRecordsProblemReport(@NonNull String credentialExchangeId,
            @NonNull V1CredentialProblemReportRequest request) throws IOException {
        Request req = buildPost(url + "/issue-credential/records/" + credentialExchangeId + "/problem-report",
                request);
        call(req);
    }

    /**
     * Send holder a credential offer in reference to a proposal with a preview
     * @param credentialExchangeId credential exchange identifier
     * @param offerRequest {@link V10CredentialBoundOfferRequest}
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialRecordsSendOffer(
            @NonNull String credentialExchangeId, @NonNull V10CredentialBoundOfferRequest offerRequest)
            throws IOException {
        Request req = buildPost(url + "/issue-credential/records/" + credentialExchangeId + "/send-offer",
                offerRequest);
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Send issuer a credential request
     * @param credentialExchangeId credential exchange identifier
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialRecordsSendRequest(
            @NonNull String credentialExchangeId) throws IOException {
        Request req = buildPost(url + "/issue-credential/records/" + credentialExchangeId + "/send-request",
                EMPTY_JSON);
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Store a received credential
     * @param credentialExchangeId the credential exchange id
     * @param request {@link V1CredentialStoreRequest}
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialRecordsStore(@NonNull String credentialExchangeId,
            @Nullable V1CredentialStoreRequest request) throws IOException {
        Request req = buildPost(url + "/issue-credential/records/" + credentialExchangeId + "/store",
                request != null ? request : "");
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Send holder a credential, automating the entire flow
     * @param request {@link V1CredentialProposalRequest} the credential to be issued
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialSend(@NonNull V1CredentialProposalRequest request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential/send", request);
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Send holder a credential offer, independent of any proposal
     * @param request {@link V1CredentialOfferRequest}
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialSendOffer(@NonNull V1CredentialOfferRequest request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential/send-offer", request);
        return call(req, V1CredentialExchange.class);
    }

    /**
     * Send issuer a credential proposal
     * @param request {@link V1CredentialProposalRequest} the requested credential
     * @return {@link V1CredentialExchange}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V1CredentialExchange> issueCredentialSendProposal(@NonNull V1CredentialProposalRequest request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential/send-proposal", request);
        return call(req, V1CredentialExchange.class);
    }

    // ----------------------------------------------------
    // Issue Credential - Credential Issue v2.0
    // ----------------------------------------------------

    /**
     * Create credential from attribute values
     * @param request {@link V20IssueCredSchemaCore}
     * @return {@link V20CredExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecord> issueCredentialV2Create(@NonNull V20IssueCredSchemaCore request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/create", request);
        return call(req, V20CredExRecord.class);
    }

    /**
     * Fetch all credential exchange records
     * @param filter {@link IssueCredentialRecordsFilter}
     * @return list of {@link V20CredExRecordDetail}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<V20CredExRecordDetail>> issueCredentialV2Records(IssueCredentialRecordsFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/issue-credential-2.0/records")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", ISSUE_CREDENTIAL_V2_TYPE);
    }

    /**
     * Fetch a single credential exchange record
     * @param credentialExchangeId credential exchange identifier
     * @return {@link V20CredExRecordDetail}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecordDetail> issueCredentialV2RecordsGetById(@NonNull String credentialExchangeId)
            throws IOException {
        Request req = buildGet(url + "/issue-credential-2.0/records/" + credentialExchangeId);
        return call(req, V20CredExRecordDetail.class);
    }

    /**
     * Remove an existing credential exchange record
     * @param credentialExchangeId the credential exchange id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void issueCredentialV2RecordsRemove(@NonNull String credentialExchangeId) throws IOException {
        Request req = buildDelete(url + "/issue-credential-2.0/records/" + credentialExchangeId);
        call(req);
    }

    /**
     * Send holder a credential
     * @param credentialExchangeId credential exchange identifier
     * @param request optional {@link V20CredIssueRequest}
     * @return {@link V20CredExRecordDetail}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecordDetail> issueCredentialV2RecordsIssue(
            @NonNull String credentialExchangeId, @Nullable V20CredIssueRequest request) throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/records/" + credentialExchangeId + "/issue",
                request != null ? request : EMPTY_JSON);
        return call(req, V20CredExRecordDetail.class);
    }

    /**
     * Send a problem report for a credential exchange
     * @param credentialExchangeId credential exchange identifier
     * @param request {@link V20CredIssueProblemReportRequest}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void issueCredentialV2RecordsProblemReport(@NonNull String credentialExchangeId,
            @NonNull V20CredIssueProblemReportRequest request) throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/records/" + credentialExchangeId + "/problem-report",
                request);
        call(req);
    }

    /**
     * Send holder a credential offer in reference to a proposal with a preview
     * @param credentialExchangeId credential exchange identifier
     * @param offerRequest {@link V20CredBoundOfferRequest}
     * @return {@link V20CredExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecord> issueCredentialV2RecordsSendOffer(
            @NonNull String credentialExchangeId, @NonNull V20CredBoundOfferRequest offerRequest) throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/records/" + credentialExchangeId + "/send-offer",
                offerRequest);
        return call(req, V20CredExRecord.class);
    }

    /**
     * Send issuer a credential request
     * @param credentialExchangeId credential exchange identifier
     * @param request {@link V20CredRequestRequest}
     * @return {@link V20CredExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecord> issueCredentialV2RecordsSendRequest(
            @NonNull String credentialExchangeId, @Nullable V20CredRequestRequest request) throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/records/" + credentialExchangeId + "/send-request",
                request != null ? request : EMPTY_JSON);
        return call(req, V20CredExRecord.class);
    }

    /**
     * Store a received credential
     * @param credentialExchangeId the credential exchange id
     * @param request {@link V20CredStoreRequest}
     * @return {@link V20CredExRecordDetail}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecordDetail> issueCredentialV2RecordsStore(@NonNull String credentialExchangeId,
            @Nullable V20CredStoreRequest request) throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/records/" + credentialExchangeId + "/store",
                request != null ? request : EMPTY_JSON);
        return call(req, V20CredExRecordDetail.class);
    }

    /**
     * Send holder a credential, automating the entire flow
     * @param request {@link V20CredSendRequest} the credential to be issued
     * @return {@link V20CredExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecord> issueCredentialV2Send(@NonNull V20CredSendRequest request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/send", request);
        return call(req, V20CredExRecord.class);
    }

    /**
     * Send holder a credential offer, independent of any proposal
     * @param request {@link V20CredOfferRequest}
     * @return {@link V20CredExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecord> issueCredentialV2SendOffer(@NonNull V20CredOfferRequest request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/send-offer", request);
        return call(req, V20CredExRecord.class);
    }

    /**
     * Send issuer a credential proposal
     * @param request {@link V20IssueCredSchemaCore} the requested credential
     * @return {@link V20CredExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecord> issueCredentialV2SendProposal(@NonNull V20IssueCredSchemaCore request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/send-proposal", request);
        return call(req, V20CredExRecord.class);
    }

    /**
     * Send issuer a credential request not bound to an existing thread. Indy credential's cannot start at a request
     * @param request {@link V20CredRequestFree} the requested credential
     * @return {@link V20CredExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20CredExRecord> issueCredentialV2SendRequest(@NonNull V20CredRequestFree request)
            throws IOException {
        Request req = buildPost(url + "/issue-credential-2.0/send-request", request);
        return call(req, V20CredExRecord.class);
    }

    // ----------------------------------------------------
    // JSON-LD
    // ----------------------------------------------------

    /**
     * Sign a JSON-LD structure and return it
     * @since aca-py 0.5.2
     * @param <T> class type either {@link VerifiableCredential} or {@link VerifiablePresentation}
     * @param signRequest {@link SignRequest}
     * @param t class type either {@link VerifiableCredential} or {@link VerifiablePresentation}
     * @return either {@link VerifiableCredential} or {@link VerifiablePresentation} with {@link Proof}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public <T> Optional<T> jsonldSign(@NonNull SignRequest signRequest, @NonNull Type t) throws IOException {
        Request req = buildPost(url + "/jsonld/sign", signRequest);
        final Optional<String> raw = raw(req);
        checkForError(raw);
        return getWrapped(raw, "signed_doc", t);
    }

    /**
     * Verify a JSON-LD structure
     * @since aca-py 0.5.2
     * @param verkey the verkey
     * @param t instance to verify either {@link VerifiableCredential} or {@link VerifiablePresentation}
     * @return {@link VerifyResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<VerifyResponse> jsonldVerify(@NonNull String verkey, @NonNull Object t) throws IOException {
        if (t instanceof VerifiableCredential || t instanceof VerifiablePresentation) {
            final JsonElement jsonTree = gson.toJsonTree(t, t.getClass());
            Request req = buildPost(url + "/jsonld/verify", new VerifyRequest(verkey, jsonTree.getAsJsonObject()));
            Optional<VerifyResponse> response = call(req, VerifyResponse.class);
            if (response.isPresent() && StringUtils.isNotEmpty(response.get().getError())) {
                throw new AriesException(0, response.get().getError());
            }
            return response;
        }
        throw new IllegalStateException("Expecting either VerifiableCredential or VerifiablePresentation");
    }

    // ----------------------------------------------------
    // Ledger
    // ----------------------------------------------------

    /**
     * Get the endpoint for a DID from the ledger.
     * @param did the DID of interest
     * @param type optional, endpoint type of interest (defaults to 'endpoint')
     * @return {@link EndpointResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<EndpointResponse> ledgerDidEndpoint(@NonNull String did,
        @Nullable DIDEndpointWithType.EndpointTypeEnum type) throws IOException{
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/ledger/did-endpoint")).newBuilder();
        b.addQueryParameter("did", did);
        if (type != null) {
            b.addQueryParameter("endpoint_type", type.getValue());
        }
        Request req = buildGet(b.build().toString());
        return call(req, EndpointResponse.class);
    }

    /**
     * Get the verkey for a did from the ledger
     * @param did the DID of interest
     * @return {@link EndpointResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<DidVerkeyResponse> ledgerDidVerkey(@NonNull String did)  throws IOException{
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/ledger/did-verkey")).newBuilder();
        b.addQueryParameter("did", did);
        Request req = buildGet(b.build().toString());
        return call(req, DidVerkeyResponse.class);
    }

    /**
     * Fetch the current transaction author agreement, if any
     * @return the current transaction author agreement, if any
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<TAAInfo> ledgerTaa() throws IOException {
        Request req = buildGet(url + "/ledger/taa");
        return getWrapped(raw(req), "result", TAAInfo.class);
    }

    /**
     * Accept the transaction author agreement
     * @param taaAccept {@link TAAAccept}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     * Or AriesException if TAA is not available
     */
    public void ledgerTaaAccept(@NonNull TAAAccept taaAccept) throws IOException {
        Request req = buildPost(url + "/ledger/taa/accept", taaAccept);
        call(req);
    }

    // ----------------------------------------------------
    // Mediation - Mediation management
    // ----------------------------------------------------

    // TODO

    // ----------------------------------------------------
    // Multitenancy - Multitenant wallet management
    // ----------------------------------------------------

    /**
     * Create sub wallet
     * @param request {@link CreateWalletRequest}
     * @return {@link WalletRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<WalletRecord> multitenancyWalletCreate(@NonNull CreateWalletRequest request) throws IOException {
        Request req = buildPost(url + "/multitenancy/wallet", request);
        return call(req, WalletRecord.class);
    }

    /**
     * Get a singe sub wallet
     * @param walletId sub wallet identifier
     * @return {@link WalletRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<WalletRecord> multitenancyWalletGet(@NonNull String walletId)
            throws IOException {
        Request req = buildGet(url + "/multitenancy/wallet/" + walletId);
        return call(req, WalletRecord.class);
    }

    /**
     * Update a sub wallet
     * @param walletId sub wallet identifier
     * @param request {@link UpdateWalletRequest}
     * @return {@link WalletRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<WalletRecord> multitenancyWalletUpdate(@NonNull String walletId,
           @NonNull UpdateWalletRequest request) throws IOException {
        Request req = buildPut(url + "/multitenancy/wallet/" + walletId, request);
        return call(req, WalletRecord.class);
    }

    /**
     * remove a sub wallet
     * @param walletId sub wallet identifier
     * @param request {@link RemoveWalletRequest}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void multitenancyWalletRemove(@NonNull String walletId,
        @NonNull RemoveWalletRequest request) throws IOException {
        Request req = buildPost(url + "/multitenancy/wallet/" + walletId + "/remove", request);
        call(req);
    }

    /**
     * Get auth token for a sub wallet
     * @param walletId sub wallet identifier
     * @param request {@link CreateWalletTokenRequest}
     * @return {@link CreateWalletTokenResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<CreateWalletTokenResponse> multitenancyWalletToken(@NonNull String walletId,
        @NonNull CreateWalletTokenRequest request) throws IOException {
        Request req = buildPost(url + "/multitenancy/wallet/" + walletId + "/token", request);
        return call(req, CreateWalletTokenResponse.class);
    }

    /**
     * Query sub wallets
     * @param walletName optional the wallets name
     * @return list of {@link WalletRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<WalletRecord>> multitenancyWallets(String walletName)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/multitenancy/wallets")).newBuilder();
        if (StringUtils.isNotEmpty(walletName)) {
            b.addQueryParameter("wallet_name", walletName);
        }
        Request req = buildGet(b.build().toString());
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", WALLET_RECORD_TYPE);
    }

    // ----------------------------------------------------
    // Out Of Band - Out-of-band connection
    // ----------------------------------------------------

    /**
     * Create a new connection invitation
     * @since aca-py 0.7.0
     * @param request {@link InvitationCreateRequest}
     * @param filter optional {@link CreateInvitationFilter}
     * @return {@link InvitationRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<InvitationRecord> outOfBandCreateInvitation(
            @NonNull InvitationCreateRequest request, CreateInvitationFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/out-of-band/create-invitation")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.build().toString(), request);
        return call(req, InvitationRecord.class);
    }

    /**
     * Receive a new connection invitation
     * @since aca-py 0.7.0
     * @param request {@link InvitationMessage}
     * @param filter {@link ReceiveInvitationFilter}
     * @return {@link ConnectionRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<ConnectionRecord> outOfBandReceiveInvitation(
            @NonNull InvitationMessage request, ReceiveInvitationFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/out-of-band/receive-invitation")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildPost(b.build().toString(), request);
        return call(req, ConnectionRecord.class);
    }

    // ----------------------------------------------------
    // Present Proof - Proof Presentation v1.0
    // ----------------------------------------------------

    /**
     * Creates a presentation request not bound to any proposal or existing connection
     * @param proofRequest {@link PresentProofRequest}
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofCreateRequest(@NonNull PresentProofRequest proofRequest)
            throws IOException {
        Request req = buildPost(url + "/present-proof/create-request", proofRequest);
        return call(req, PresentationExchangeRecord.class);
    }

    /**
     * Fetch all present-proof exchange records
     * @return list of {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<PresentationExchangeRecord>> presentProofRecords() throws IOException {
        return presentProofRecords(null);
    }

    /**
     * Fetch all present-proof exchange records
     * @param filter {@link PresentProofRecordsFilter}
     * @return list of {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<PresentationExchangeRecord>> presentProofRecords(@Nullable PresentProofRecordsFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/present-proof/records")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", PROOF_TYPE);
    }

    /**
     * Fetch a single presentation exchange record by ID
     * @param presentationExchangeId the presentation exchange id
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofRecordsGetById(@NonNull String presentationExchangeId)
            throws IOException {
        Request req = buildGet(url + "/present-proof/records/" + presentationExchangeId);
        return call(req, PresentationExchangeRecord.class);
    }

    /**
     * Remove an existing presentation exchange record by ID
     * @param presentationExchangeId the presentation exchange id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void presentProofRecordsRemove(@NonNull String presentationExchangeId) throws IOException {
        Request req = buildDelete(url + "/present-proof/records/" + presentationExchangeId);
        call(req);
    }

    /**
     * Fetch credentials for a presentation request from wallet
     * @param presentationExchangeId the presentation exchange id
     * @return list of {@link PresentationRequestCredentials}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<PresentationRequestCredentials>> presentProofRecordsCredentials(
            @NonNull String presentationExchangeId) throws IOException {
        return presentProofRecordsCredentials(presentationExchangeId, null);
    }

    /**
     * Fetch credentials for a presentation request from wallet
     * @param presentationExchangeId the presentation exchange id
     * @param filter {@link PresentationRequestCredentialsFilter}
     * @return list of {@link PresentationRequestCredentials}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<PresentationRequestCredentials>> presentProofRecordsCredentials(
            @NonNull String presentationExchangeId, @Nullable PresentationRequestCredentialsFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl
                .parse(url + "/present-proof/records/" + presentationExchangeId + "/credentials")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        return call(req, PRESENTATION_REQUEST_CREDENTIALS);
    }

    /**
     * Send a problem report for presentation exchange
     * @param presentationExchangeId presentation exchange identifier
     * @param request {@link V10PresentationProblemReportRequest}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void presentProofRecordsProblemReport(@NonNull String presentationExchangeId,
        @NonNull V10PresentationProblemReportRequest request) throws IOException {
        Request req = buildPost(url + "/present-proof/records/" + presentationExchangeId + "/problem-report",
                request);
        call(req);
    }

    /**
     * Sends a proof presentation
     * @param presentationExchangeId the presentation exchange id
     * @param presentationRequest {@link PresentationRequest}
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofRecordsSendPresentation(@NonNull String presentationExchangeId,
        @NonNull PresentationRequest presentationRequest) throws IOException {
        Request req = buildPost(url + "/present-proof/records/" + presentationExchangeId + "/send-presentation",
                presentationRequest);
        return call(req, PresentationExchangeRecord.class);
    }

    /**
     * Sends presentation request in reference to a proposal
     * @param presentationExchangeId presentation exchange identifier
     * @param request {@link AdminAPIMessageTracing}
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofRecordsSendRequest(
            @NonNull String presentationExchangeId, @NonNull AdminAPIMessageTracing request) throws IOException{
        Request req = buildPost(url + "/present-proof/records/" + presentationExchangeId + "/send-request",
                request);
        return call(req, PresentationExchangeRecord.class);
    }

    /**
     * Verify a received presentation
     * @param presentationExchangeId presentation exchange identifier
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofRecordsVerifyPresentation(
            @NonNull String presentationExchangeId) throws IOException{
        Request req = buildPost(url + "/present-proof/records/" + presentationExchangeId + "/verify-presentation",
                EMPTY_JSON);
        return call(req, PresentationExchangeRecord.class);
    }

    /**
     * Sends a presentation proposal
     * @param proofProposal {@link PresentProofProposal}
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofSendProposal(@NonNull PresentProofProposal proofProposal)
            throws IOException{
        Request req = buildPost(url + "/present-proof/send-proposal", proofProposal);
        return call(req, PresentationExchangeRecord.class);
    }

    /**
     * Sends a free presentation request not bound to any proposal
     * @param proofRequest {@link PresentProofRequest}
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofSendRequest(@NonNull PresentProofRequest proofRequest)
            throws IOException {
        Request req = buildPost(url + "/present-proof/send-request", proofRequest);
        return call(req, PresentationExchangeRecord.class);
    }

    /**
     * Sends a free presentation request not bound to any proposal. Use this method if you want to have full
     * control over the proof request.
     * @param proofRequestJson json string
     * @return {@link PresentationExchangeRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PresentationExchangeRecord> presentProofSendRequest(@NonNull String proofRequestJson)
            throws IOException {
        JsonObject proofRequest = gson.fromJson(proofRequestJson, JsonObject.class);
        Request req = buildPost(url + "/present-proof/send-request", proofRequest);
        return call(req, PresentationExchangeRecord.class);
    }

    // ----------------------------------------------------
    // Present-Proof v2.0 - Proof presentation v2.0
    // ----------------------------------------------------

    /**
     * Creates a presentation request not bound to any proposal or existing connection
     * @param request {@link V20PresCreateRequestRequest}
     * @return {@link V20PresExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20PresExRecord> presentProofV2CreateRequest(
            @NonNull V20PresCreateRequestRequest request) throws IOException {
        Request req = buildPost(url + "/present-proof-2.0/create-request", request);
        return call(req, V20PresExRecord.class);
    }

    /**
     * Fetch all present-proof exchange records
     * @param filter {@link PresentProofRecordsFilter}
     * @return list of {@link V20PresExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<V20PresExRecord>> presentProofV2Records(@Nullable PresentProofRecordsFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/present-proof-2.0/records")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        final Optional<String> resp = raw(req);
        return getWrapped(resp, "results", PROOF_TYPE_V2);
    }

    /**
     * Fetch a single presentation exchange record by ID
     * @param presentationExchangeId the presentation exchange id
     * @return {@link V20PresExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20PresExRecord> presentProofV2RecordsGetById(@NonNull String presentationExchangeId)
            throws IOException {
        Request req = buildGet(url + "/present-proof-2.0/records/" + presentationExchangeId);
        return call(req, V20PresExRecord.class);
    }

    /**
     * Remove an existing presentation exchange record by ID
     * @param presentationExchangeId the presentation exchange id
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void presentProofV2RecordsRemove(@NonNull String presentationExchangeId) throws IOException {
        Request req = buildDelete(url + "/present-proof-2.0/records/" + presentationExchangeId);
        call(req);
    }

    /**
     * Fetch credentials for a presentation request from wallet
     * @param presentationExchangeId the presentation exchange id
     * @param filter {@link PresentationRequestCredentialsFilter}
     * @return list of {@link PresentationRequestCredentials}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<PresentationRequestCredentials>> presentProofV2RecordsCredentials(
            @NonNull String presentationExchangeId, @Nullable PresentationRequestCredentialsFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl
                .parse(url + "/present-proof-2.0/records/" + presentationExchangeId + "/credentials")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.build().toString());
        return call(req, PRESENTATION_REQUEST_CREDENTIALS);
    }

    /**
     * Send a problem report for presentation exchange
     * @param presentationExchangeId presentation exchange identifier
     * @param request {@link V20PresProblemReportRequest}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void presentProofV2RecordsProblemReport(@NonNull String presentationExchangeId,
            @NonNull V20PresProblemReportRequest request) throws IOException {
        Request req = buildPost(url + "/present-proof-2.0/records/" + presentationExchangeId + "/problem-report",
                request);
        call(req);
    }

    /**
     * Sends a proof presentation
     * @param presentationExchangeId the presentation exchange id
     * @param presentationRequest {@link V20PresSpecByFormatRequest}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20PresExRecord> presentProofV2RecordsSendPresentation(@NonNull String presentationExchangeId,
            @NonNull V20PresSpecByFormatRequest presentationRequest) throws IOException {
        Request req = buildPost(url + "/present-proof-2.0/records/" + presentationExchangeId + "/send-presentation",
                presentationRequest);
        return call(req, V20PresExRecord.class);
    }

    /**
     * Sends presentation request in reference to a proposal
     * @param presentationExchangeId presentation exchange identifier
     * @param request {@link AdminAPIMessageTracing}
     * @return {@link V20PresExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20PresExRecord> presentProofV2RecordsSendRequest(
            @NonNull String presentationExchangeId, @NonNull AdminAPIMessageTracing request) throws IOException{
        Request req = buildPost(url + "/present-proof-2.0/records/" + presentationExchangeId + "/send-request",
                request);
        return call(req, V20PresExRecord.class);
    }

    /**
     * Verify a received presentation
     * @param presentationExchangeId presentation exchange identifier
     * @return {@link V20PresExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20PresExRecord> presentProofV2RecordsVerifyPresentation(
            @NonNull String presentationExchangeId) throws IOException{
        Request req = buildPost(url + "/present-proof-2.0/records/" + presentationExchangeId + "/verify-presentation",
                EMPTY_JSON);
        return call(req, V20PresExRecord.class);
    }

    /**
     * Sends a presentation proposal
     * @param proofProposal {@link V20PresProposalRequest}
     * @return {@link V20PresExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20PresExRecord> presentProofV2SendProposal(@NonNull V20PresProposalRequest proofProposal)
            throws IOException{
        Request req = buildPost(url + "/present-proof-2.0/send-proposal", proofProposal);
        return call(req, V20PresExRecord.class);
    }

    /**
     * Sends a free presentation request not bound to any proposal
     * @param proofRequest {@link V20PresSendRequestRequest}
     * @return {@link V20PresExRecord}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<V20PresExRecord> presentProofV2SendRequest(@NonNull V20PresSendRequestRequest proofRequest)
            throws IOException {
        Request req = buildPost(url + "/present-proof-2.0/send-request", proofRequest);
        return call(req, V20PresExRecord.class);
    }

    // ----------------------------------------------------
    // Resolver - Retrieve doc for requested did
    // ----------------------------------------------------

    /**
     * Retrieve doc for requested did
     * @since aca-py 0.7.0
     * @param did the did
     * @return {@link DIDDocument}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<DIDDocument> resolverResolveDid(@NonNull String did) throws IOException {
        Request req = buildGet(url + "/resolver/resolve/" + did);
        return getWrapped(raw(req), "did_document", DIDDocument.class);
    }

    // ----------------------------------------------------
    // Revocation
    // ----------------------------------------------------

    /**
     * Get an active revocation registry by credential definition id
     * @param credentialDefinitionId the credential definition id
     * @return {@link RevRegCreateResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<RevRegCreateResponse> revocationActiveRegistry(@NonNull String credentialDefinitionId)
            throws IOException {
        Request req = buildGet(url + "/revocation/active-registry/" + credentialDefinitionId);
        return getWrapped(raw(req), "result", RevRegCreateResponse.class);
    }

    /**
     * Clear pending revocations
     * @param request {@link ClearPendingRevocationsRequest} Credential revocation ids by revocation registry id:
     * omit for all, specify null or empty list for all pending per revocation registry
     * @return {@link PublishRevocations}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PublishRevocations> revocationClearPendingRevocations(
            @NonNull ClearPendingRevocationsRequest request)
            throws IOException {
        Request req = buildPost(url + "/revocation/clear-pending-revocations", request);
        return call(req, PublishRevocations.class);
    }

    /**
     * Creates a new revocation registry
     * Creating a new registry is a three-step flow:
     * First: create the registry
     * Second: publish the URI of the tails file {@link #revocationRegistryUpdateUri}
     * Third: Set the registry to active {@link #revocationActiveRegistry}
     * @param revRegRequest {@link RevRegCreateRequest}
     * @return {@link RevRegCreateResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<RevRegCreateResponse> revocationCreateRegistry(@NonNull RevRegCreateRequest revRegRequest)
            throws IOException {
        Request req = buildPost(url + "/revocation/create-registry", revRegRequest);
        return getWrapped(raw(req), "result", RevRegCreateResponse.class);
    }

    /**
     * Publish pending revocations to ledger
     * @param request {@link PublishRevocations}
     * @return {@link PublishRevocations}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PublishRevocations> revocationPublishRevocations(@NonNull PublishRevocations request)
            throws IOException {
        Request req = buildPost(url + "/revocation/publish-revocations", request);
        return call(req, PublishRevocations.class);
    }

    /**
     * Publish pending revocations to ledger via an endorser
     * @param request {@link PublishRevocations}
     * @return {@link TxnOrPublishRevocationsResult}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<TxnOrPublishRevocationsResult> revocationPublishRevocations(@NonNull PublishRevocations request, @NonNull EndorserInfoFilter filter)
            throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl
                .parse(url + "/revocation/publish-revocations")).newBuilder();
        filter.buildParams(b);
        Request req = buildPost(b.toString(), request);
        return call(req, TxnOrPublishRevocationsResult.class);
    }

    /**
     * Search for matching revocation registries that current agent created
     * @param credentialDefinitionId the credential definition id
     * @param state {@link RevocationRegistryState}
     * @return {@link RevRegsCreated}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<RevRegsCreated> revocationRegistriesCreated(
            @Nullable String credentialDefinitionId, @Nullable RevocationRegistryState state)
            throws IOException{
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl.parse(url + "/revocation/registries/created")).newBuilder();
        if (StringUtils.isNotEmpty(credentialDefinitionId)) {
            b.addQueryParameter("cred_def_id", credentialDefinitionId);
        }
        if (state != null) {
            b.addQueryParameter("state", state.getValue());
        }
        Request req = buildGet(b.build().toString());
        return call(req, RevRegsCreated.class);
    }

    /**
     * Gets revocation registry by revocation registry id
     * @param revRegId the revocation registry id
     * @return {@link RevRegCreateResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<RevRegCreateResponse> revocationRegistryGetById(@NonNull String revRegId)
            throws IOException {
        Request req = buildGet(url + "/revocation/registry/" + revRegId);
        return getWrapped(raw(req), "result", RevRegCreateResponse.class);
    }

    /**
     * Update revocation registry with new public URI to the tails file.
     * @param revRegId the revocation registry id
     * @param tailsFileUri {@link RevRegUpdateTailsFileUri} the URI of the tails file
     * @return {@link RevRegCreateResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<RevRegCreateResponse> revocationRegistryUpdateUri(
            @NonNull String revRegId, @NonNull RevRegUpdateTailsFileUri tailsFileUri)
            throws IOException {
        Request req = buildPatch(url + "/revocation/registry/" + revRegId, tailsFileUri);
        return getWrapped(raw(req), "result", RevRegCreateResponse.class);
    }

    /**
     * Revoke an issued credential
     * @param revokeRequest {@link RevokeRequest}
     * @return empty object when success
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<RevRegCreateResponse.RevocationModuleResponse> revocationRevoke(@NonNull RevokeRequest revokeRequest)
            throws IOException {
        Request req = buildPost(url + "/revocation/revoke", revokeRequest);
        return call(req, RevRegCreateResponse.RevocationModuleResponse.class);
    }

    // ----------------------------------------------------
    // Schema - Schema operations
    // ----------------------------------------------------

    /**
     * Sends a schema to the ledger
     * @param schema {@link SchemaSendRequest}
     * @return {@link SchemaSendResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<SchemaSendResponse> schemas(@NonNull SchemaSendRequest schema) throws IOException {
        Request req = buildPost(url + "/schemas", schema);
        return call(req, SchemaSendResponse.class);
    }

    /**
     * Sends a schema to the ledger via an endorser
     * @since aca-py 0.7.0
     * @param schema {@link SchemaSendRequest}
     * @param endorserInfoFilter {@link EndorserInfoFilter}
     * @return {@link TxnOrSchemaSendResult}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<TxnOrSchemaSendResult> schemas(
            @NonNull SchemaSendRequest schema, @NonNull EndorserInfoFilter endorserInfoFilter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl
                .parse(url + "/schemas")).newBuilder();
        endorserInfoFilter.buildParams(b);
        Request req = buildPost(b.toString(), schema);
        return call(req, TxnOrSchemaSendResult.class);
    }

    /**
     * Gets a schema from the ledger
     * @param schemaId the schemas id or sequence number
     * @return {@link Schema}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<Schema> schemasGetById(@NonNull String schemaId) throws IOException {
        Request req = buildGet(url + "/schemas/" + schemaId);
        return getWrapped(raw(req), "schema", Schema.class);
    }
    
    /**
     * Loads all schemas from the ledger, which where created by the DID of this cloudagent.
     * 
     * @param filter allows looking only for some schemas
     * @return List of Schema names
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<String>> schemasCreated(@Nullable SchemasCreatedFilter filter) throws IOException {
        HttpUrl.Builder b = Objects.requireNonNull(HttpUrl
                .parse(url + "/schemas/created")).newBuilder();
        if (filter != null) {
            filter.buildParams(b);
        }
        Request req = buildGet(b.toString());
        return getWrapped(raw(req), "schema_ids", List.class);
    }

    // ----------------------------------------------------
    // Server
    // ----------------------------------------------------

    /**
     * Fetch the server configuration
     * @since aca-py 0.7.0
     * @return {@link AdminConfig}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<AdminConfig> statusConfig() throws IOException {
        Request req = buildGet(url + "/status/config");
        return call(req, AdminConfig.class);
    }

    /**
     * Server liveliness check
     * @since aca-py 0.5.3
     * @return {@link AdminStatusLiveliness}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<AdminStatusLiveliness> statusLive() throws IOException {
        Request req = buildGet(url + "/status/live");
        return call(req, AdminStatusLiveliness.class);
    }

    /**
     * Server readiness check
     * @since aca-py 0.5.3
     * @return {@link AdminStatusReadiness}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<AdminStatusReadiness> statusReady() throws IOException {
        Request req = buildGet(url + "/status/ready");
        return call(req, AdminStatusReadiness.class);
    }

    /**
     * Helper that blocks until either a timeout is reached or aca-py returns that it is ready
     * @since aca-py 0.5.3
     * @param timeout {@link Duration} how long to wait for aca-py to be ready until failing
     */
    public void statusWaitUntilReady(@NonNull Duration timeout) {
        Instant to = Instant.now().plus(timeout);
        while(Instant.now().isBefore(to)) {
            try {
                final Optional<AdminStatusReadiness> statusReady = this.statusReady();
                if (statusReady.isPresent() && statusReady.get().isReady()) {
                    log.info("aca-py is ready");
                    return;
                }
            } catch (IOException e) {
                log.trace("aca-py not ready yet, reason: {}", e.getMessage());
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error("Interrupted while waiting for aca-py", e);
            }
        }
        String msg = "Timeout exceeded, aca-py not ready after: " + timeout;
        log.error(msg);
        throw new AriesException(0, msg);
    }

    // ----------------------------------------------------
    // Trust Ping - Trust-ping Over Connection
    // ----------------------------------------------------

    /**
     * Send a trust ping to a connection
     * @param connectionId the connection id
     * @param comment comment for the ping message
     * @return {@link PingResponse}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<PingResponse> connectionsSendPing(@NonNull String connectionId, @NonNull PingRequest comment)
            throws IOException {
        Request req = buildPost(url + "/connections/" + connectionId + "/send-ping", comment);
        return call(req, PingResponse.class);
    }

    // ----------------------------------------------------
    // Wallet
    // ----------------------------------------------------

    /**
     * List wallet DIDs
     * @return list of {@link DID}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<List<DID>> walletDid() throws IOException {
        Request req = buildGet(url + "/wallet/did");
        return getWrapped(raw(req), "results", WALLET_DID_TYPE);
    }

    /**
     * Create local DID
     * @param didCreate {@link DIDCreate}
     * @return {@link DID}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<DID> walletDidCreate(@NonNull DIDCreate didCreate) throws IOException {
        Request req = buildPost(url + "/wallet/did/create", didCreate);
        return getWrapped(raw(req), "result", DID.class);
    }

    /**
     * Fetch the current public DID
     * @return {@link DID}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<DID> walletDidPublic() throws IOException {
        Request req = buildGet(url + "/wallet/did/public");
        return getWrapped(raw(req), "result", DID.class);
    }

    /**
     * Query DID end point in wallet
     * @param did the did
     * @return {@link DIDEndpoint}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public Optional<DIDEndpoint> walletGetDidEndpoint(@NonNull String did) throws IOException {
        Request req = buildGet(url + "/wallet/get-did-endpoint" + "?did=" + did);
        return call(req, DIDEndpoint.class);
    }

    /**
     * Update end point in wallet and, if public, on ledger
     * @param endpointRequest {@link DIDEndpointWithType}
     * @throws IOException if the request could not be executed due to cancellation, a connectivity problem or timeout.
     */
    public void walletSetDidEndpoint(@NonNull DIDEndpointWithType endpointRequest) throws IOException {
        Request req = buildPost(url + "/wallet/set-did-endpoint", endpointRequest);
        call(req);
    }

    // ----------------------------------------------------
    // Internal
    // ----------------------------------------------------

    private Request buildPost(String u, Object body) {
        return request(u)
                .post(jsonBody(gson.toJson(body)))
                .build();
    }

    private Request buildPut(String u, Object body) {
        return request(u)
                .put(jsonBody(gson.toJson(body)))
                .build();
    }

    private Request buildPatch(String u, Object body) {
        return request(u)
                .patch(jsonBody(gson.toJson(body)))
                .build();
    }

    private Request buildGet(String u) {
        return request(u)
                .get()
                .build();
    }

    private Request buildDelete(String u) {
        return request(u)
                .delete()
                .build();
    }

    private Request.Builder request(String u) {
        Request.Builder b = new Request.Builder()
                .url(u)
                .header(X_API_KEY, apiKey);
        if (StringUtils.isNotEmpty(bearerToken)) {
            b.header(AUTHORIZATION, BEARER + bearerToken);
        }
        return b;
    }
}
