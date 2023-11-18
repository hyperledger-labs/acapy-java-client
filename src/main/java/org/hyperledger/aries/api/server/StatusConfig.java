/*
 * Copyright (c) 2020-2023 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * Typed admin config, might not be complete
 */
@Data
@NoArgsConstructor
public class StatusConfig {

    // Admin
    @SerializedName("admin.admin_insecure_mode")
    private Boolean adminInsecureMode;
    @SerializedName("admin.enabled")
    private Boolean adminEnabled;
    @SerializedName("admin.host")
    private String adminHost;
    @SerializedName("admin.port")
    private String adminPort;
    @SerializedName("admin.webhook_urls")
    private List<String> adminWebhookUrls;
    @SerializedName("admin.admin_client_max_request_size")
    private Integer adminClientMaxRequestSize;

    // Auto
    private Boolean autoDiscloseFeatures;
    private Boolean autoPingConnection;
    private Boolean autoProvision;
    @SerializedName("debug.auto_accept_invites")
    private Boolean autoAcceptInvites;
    @SerializedName("debug.auto_accept_requests")
    private Boolean autoAcceptRequests;
    @SerializedName("debug.auto_respond_messages")
    private Boolean autoRespondMessages;
    @SerializedName("debug.auto_respond_credential_proposal")
    private Boolean autoRespondCredentialProposal;
    @SerializedName("debug.auto_respond_credential_offer")
    private Boolean autoRespondCredentialOffer;
    @SerializedName("debug.auto_respond_credential_request")
    private Boolean autoRespondCredentialRequest;
    @SerializedName("debug.auto_respond_presentation_request")
    private Boolean autoRespondPresentationRequest;
    @SerializedName("debug.auto_verify_presentation")
    private Boolean autoVerifyPresentation;
    @SerializedName("debug.auto_respond_presentation_proposal")
    private Boolean autoRespondPresentationProposal;
    @SerializedName("debug.auto_store_credential")
    private Boolean autoStoreCredential;


    private List<String> externalPlugins;
    private String defaultEndpoint;
    private List<String> additionalEndpoints;

    private String tailsServerBaseUrl;
    private String tailsServerUploadUrl;

    @SerializedName("revocation.notify")
    private Boolean revocationNotify;
    @SerializedName("revocation.monitor_notification")
    private Boolean revocationMonitorRevocation;

    @SerializedName("ledger.ledger_config_list")
    private List<LedgerConfigEntry> ledgerConfigList;

    @SerializedName("ledger.keepalive")
    private Integer ledgerKeepalive;

    @SerializedName("log.level")
    private String logLevel;

    @SerializedName("debug.monitor_ping")
    private Boolean monitorPing;

    private Boolean publicInvites;

    @SerializedName("trace.target")
    private String traceTarget;
    @SerializedName("trace.tag")
    private String traceTag;
    @SerializedName("trace.label")
    private String traceLabel;

    private Boolean preserveExchangeRecords;

    private Boolean emitNewDidcomPrefix;
    private Boolean emitNewDidcomMimeType;
    private Boolean exchUseUnencryptedTags;

    // Transport
    @SerializedName("transport.inbound_configs")
    private List<List<String>> transportInboundConfigs;
    @SerializedName("transport.outbound_configs")
    private List<String> transportOutboundConfigs;
    @SerializedName("transport.enable_undelivered_queue")
    private Boolean transportEnableUndeliveredQueue;
    @SerializedName("transport.max_message_size")
    private Integer transportMaxMessageSize;
    @SerializedName("transport.max_outbound_retry")
    private Integer transportMaxOutboundRetry;
    @SerializedName("transport.ws.heartbeat_interval")
    private Integer transportWsHeartbeatInterval;
    @SerializedName("transport.ws.timeout_interval")
    private Integer transportWsTimeoutInterval;

    private String defaultLabel;

    // Wallet
    @SerializedName("wallet.name")
    private String walletName;
    @SerializedName("wallet.storage_type")
    private String walletStorageType;
    @SerializedName("wallet.type")
    private String walletType;
    @SerializedName("wallet.storage_config")
    private String walletStorageConfig;


    // Endorser
    @SerializedName("endorser.author")
    private Boolean endorserAuthor;
    @SerializedName("endorser.endorser")
    private Boolean endorserEndorser;
    @SerializedName("endorser.auto_endorse")
    private Boolean endorserAutoEndorse;
    @SerializedName("endorser.auto_write")
    private Boolean endorserAutoWrite;
    @SerializedName("endorser.auto_create_rev_reg")
    private Boolean endorserAutoCreateRevReg;
    @SerializedName("endorser.auto_promote_author_did")
    private Boolean endorserAutoPromoteAuthorDid;

    @SerializedName("ledger.read_only")
    private Boolean ledgerReadOnly;

    public boolean isTailsServerConfigured() {
        return StringUtils.isNotBlank(tailsServerBaseUrl);
    }

    public boolean isAutoAcceptInvites() {
        return Boolean.TRUE.equals(autoAcceptInvites);
    }

    public boolean isAutoAcceptRequests() {
        return Boolean.TRUE.equals(autoAcceptRequests);
    }

    public boolean isAutoRespondMessages() {
        return Boolean.TRUE.equals(autoRespondMessages);
    }

    public boolean isAutoRespondCredentialOffer() {
        return Boolean.TRUE.equals(autoRespondCredentialOffer);
    }

    public boolean isAutoRespondCredentialProposal() {
        return Boolean.TRUE.equals(autoRespondCredentialProposal);
    }

    public boolean isAutoRespondCredentialRequest() {
        return Boolean.TRUE.equals(autoRespondCredentialRequest);
    }

    public boolean isAutoRespondPresentationProposal() {
        return Boolean.TRUE.equals(autoRespondPresentationProposal);
    }

    public boolean isAutoRespondPresentationRequest() {
        return Boolean.TRUE.equals(autoRespondPresentationRequest);
    }

    public boolean isAutoStoreCredential() {
        return Boolean.TRUE.equals(autoStoreCredential);
    }

    public boolean isAutoVerifyPresentation() {
        return Boolean.TRUE.equals(autoVerifyPresentation);
    }

    public boolean isPreserveExchangeRecords() {
        return Boolean.TRUE.equals(preserveExchangeRecords);
    }

    public Optional<LedgerConfigEntry> findWriteLedger() {
        if (ledgerConfigList != null) {
            return ledgerConfigList.stream()
                    .filter(LedgerConfigEntry::isWriteLedger)
                    .findFirst();
        }
        return Optional.empty();
    }

    @Data
    @NoArgsConstructor
    public static final class LedgerConfigEntry {
        private String id;
        private Boolean isProduction;
        private Boolean isWrite;
        private String genesisTransactions;
        private Integer keepalive;
        private Boolean readOnly;
        private String socksProxy;
        private String poolName;

        @JsonIgnore
        public boolean isProductionLedger() {
            return Boolean.TRUE.equals(isProduction);
        }

        @JsonIgnore
        public boolean isWriteLedger() {
            return Boolean.TRUE.equals(isWrite);
        }

        @JsonIgnore
        public boolean isReadOnly() {
            return Boolean.TRUE.equals(readOnly);
        }
    }
}
