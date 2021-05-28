/*
 * Copyright (c) 2020-2021 - for information on the respective copyright owner
 * see the NOTICE file and/or the repository at
 * https://github.com/hyperledger-labs/acapy-java-client
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.aries.api.present_proof;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Connection less present proof request
 *
 * @see <a href="https://github.com/hyperledger/aries-rfcs/tree/master/features/0037-present-proof#request-presentation">
 * 0037-present-proof#request-presentation</a>
 */
@Data
public class ProofRequestPresentation {

    @SerializedName("@id")
    private String id;

    @SerializedName("@type")
    private String type = "https://didcomm.org/present-proof/1.0/request-presentation";

    private String comment = "";

    @SerializedName("~service")
    private ServiceDecorator service;

    @SerializedName("request_presentations~attach")
    private List<PresentationAttachment> request;

    @SerializedName("~thread")
    private Thread thread;

    public ProofRequestPresentation(String ariesUri, String verkey, String threadId, String proofRequest) {
        this.id = threadId;
        this.service = new ServiceDecorator(ariesUri, verkey);
        this.request = List.of(new PresentationAttachment(proofRequest));
        this.thread = new Thread(threadId);
    }

    @Data
    public static class PresentationAttachment {
        @SerializedName("@id")
        private String id = "libindy-request-presentation-0";

        @SerializedName("mime-type")
        private String mimeType = "application/json";

        private Map<String, String> data;

        public PresentationAttachment(String proofRequest) {
            this.data = Map.of("base64", proofRequest);
        }
    }

    @Data
    public static class ServiceDecorator {

        @SerializedName("recipientKeys")
        private List<String> recipientKeys;

        @SerializedName("routingKeys")
        private List<String> routingKeys = List.of();

        @SerializedName("serviceEndpoint")
        private String serviceEndpoint;

        public ServiceDecorator(String ariesUri, String verkey) {
            this.serviceEndpoint = ariesUri;
            this.recipientKeys = List.of(verkey);
        }
    }

    @Data
    public static class Thread {
        private String thid;
        private Integer senderOrder = 0;
        private Object receivedOrders;

        public Thread(String thid) {
            this.thid = thid;
        }
    }
}
