# ACA-PY Java Client Library

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![CI/CD](https://github.com/hyperledger-labs/acapy-java-client/workflows/CI/CD/badge.svg)](https://github.com/hyperledger-labs/acapy-java-client/actions?query=workflow%3ACI%2FCD+branch%3Amaster)
[![Maven](https://img.shields.io/maven-central/v/network.idu.acapy/aries-client-python)](Maven Central)

Convenience library based on okhttp and gson to interact with [aries cloud agent python](https://github.com/hyperledger/aries-cloudagent-python) (aca-py) instances.  
It is currently work in progress and not all endpoints of the agent are present in the client.

## Use it in your project

```xml
<dependency>
   <groupId>network.idu.acapy</groupId>
   <artifactId>aries-client-python</artifactId>
   <version>0.7.0-pre.3.2</version>
</dependency>
```

For a aca-py 0.6.0 compatible client version you can use the following repository:

```xml
<repositories>
    <repository>
        <id>acapy-java-client</id>
        <url>https://nexus.bosch-digital.com/repository/bds-all</url>
    </repository>
</repositories>
```
```xml
<dependency>
    <groupId>org.hyperledger</groupId>
    <artifactId>aries-client-python</artifactId>
    <version>0.23.0</version>
</dependency>
```

## FAQ

1. Why don't you use swagger codegen?

   For a long time aca-py's swagger.json was not really in sync with the code base. This has been hugely improved lately,
   so I started to generate model classes based on the stable releases found on dockerhub. There are still issues
   with complex structures, so one can not simply use the models 1:1, instead each one has to be checked manually before using it.
   This is tedious work and might take a while to complete. Also, the api is complex so that I found it useful to introduce
   helper methods directly in the model classes to make them more accessible.

2. Why is endpoint X, or field Y missing?

   aca-py's api is changing rapidly with each release, and until most of the classes are using the generated models this can happen.
   So, if you are missing something create a PR with a fix or open an issue.

## Version Compatibility

| Client Version | ACA-PY Version  |
|----------------|-----------------|
| 0.7.0-pre.2    | 0.7.0-pre.2     |
| 0.7.0-pre.3.2  | 0.7.0-pre.3     |

## Implemented Endpoints

| Method | Endpoint                                                | Implemented        |
|--------|---------------------------------------------------------|--------------------|
|        | **action-menu**                                         |                    |
| POST   | /action-menu/{conn_id}/close                            | :white_check_mark: |
| POST   | /action-menu/{conn_id}/fetch                            | :white_check_mark: |
| POST   | /action-menu/{conn_id}/perform                          | :white_check_mark: |
| POST   | /action-menu/{conn_id}/request                          | :white_check_mark: |
| POST   | /action-menu/{conn_id}/send-menu                        | :white_check_mark: |
|        | **basicmessage**                                        |                    |
| POST   | /connections/{conn_id}/send-message                     | :white_check_mark: |
|        | **connection**                                          |                    |
| GET    | /connections                                            | :white_check_mark: |
| POST   | /connections/create-invitation                          | :white_check_mark: |
| POST   | /connections/create-static                              | :white_check_mark: |
| POST   | /connections/receive-invitation                         | :white_check_mark: |
| GET    | /connections/{conn_id}}                                 | :white_check_mark: |
| DELETE | /connections/{conn_id}                                  | :white_check_mark: |
| POST   | /connections/{conn_id}/accept-invitation                | :white_check_mark: |
| POST   | /connections/{conn_id}/accept-request                   | :white_check_mark: |
| GET    | /connections/{conn_id}/endpoints                        | :white_check_mark: |
| POST   | /connections/{conn_id}/establish-inbound/{ref_id}       | :white_check_mark: |
| GET    | /connections/{conn_id}/metadata                         | :white_check_mark: |
| POST   | /connections/{conn_id}/metadata                         | :white_check_mark: |
|        | **credential-definition**                               |                    |
| POST   | /credential-definitions                                 | :white_check_mark: |
| GET    | /credential-definitions/created                         | :white_check_mark: |
| GET    | /credential-definitions/{cred_def_id}                   | :white_check_mark: |
|        | **credentials**                                         |                    |
| GET    | /credentials/mime-types/{credential_id}                 | :white_check_mark: |
| GET    | /credentials/revoked/{credential_id}                    | :white_check_mark: |
| GET    | /credential/{credential_id}                             | :white_check_mark: |
| DELETE | /credential/{credential_id}                             | :white_check_mark: |
| GET    | /credentials                                            | :white_check_mark: |
|        | **did-exchange**                                        |                    |
| POST   | /didexchange/create-request                             | :white_check_mark: |
| POST   | /didexchange/receive-request                            | :white_check_mark: |
| POST   | /didexchange/{conn_id}/accept-invitation                | :white_check_mark: |
| POST   | /didexchange/{conn_id}/accept-request                   | :white_check_mark: |
|        | **endorse-transaction**                                 |                    |
| POST   | /transaction/{tran_id}/resend                           | :white_check_mark: |
| POST   | /transactions                                           | :white_check_mark: |
| POST   | /transactions/create-request                            | :white_check_mark: |
| POST   | /transactions/{conn_id}/set-endorser-info               | :white_check_mark: |
| POST   | /transactions/{conn_id}/set-endorser-role               | :white_check_mark: |
| POST   | /transactions/{tran_id}                                 | :white_check_mark: |
| POST   | /transactions/{tran_id}/cancel                          | :white_check_mark: |
| POST   | /transactions/{tran_id}/endorse                         | :white_check_mark: |
| POST   | /transactions/{tran_id}/refuse                          | :white_check_mark: |
| POST   | /transactions/{tran_id}/write                           | :white_check_mark: |
|        | **introduction**                                        |                    |
|        | **issue-credential v1.0**                               |                    |
| POST   | /issue-credential/create                                | :white_check_mark: |
| GET    | /issue-credential/records                               | :white_check_mark: |
| GET    | /issue-credential/records/{cred_ex_id}                  | :white_check_mark: |
| DELETE | /issue-credential/records/{cred_ex_id}                  | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/issue            | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/problem-report   | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/send-offer       | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/send-request     | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/store            | :white_check_mark: |
| POST   | /issue-credential/send                                  | :white_check_mark: |
| POST   | /issue-credential/send-offer                            | :white_check_mark: |
| POST   | /issue-credential/send-proposal                         | :white_check_mark: |
|        | **issue-credential v2.0**                               |                    |
|        | **jsonld**                                              |                    |
| POST   | /jsonld/sign                                            | :white_check_mark: |
| POST   | /jsonld/verify                                          | :white_check_mark: |
|        | **ledger**                                              |                    |
| GET    | /ledger/did-endpoint                                    | :white_check_mark: |
| GET    | /ledger/did-verkey                                      | :white_check_mark: |
| GET    | /ledger/taa                                             | :white_check_mark: |
| POST   | /ledger/taa/accept                                      | :white_check_mark: |
|        | **mediation**                                           |                    |
|        | **multitenancy**                                        |                    |
| POST   | /multitenancy/wallet                                    | :white_check_mark: |
| GET    | /multitenancy/wallet/{wallet_id}                        | :white_check_mark: |
| PUT    | /multitenancy/wallet/{wallet_id}                        | :white_check_mark: |
| POST   | /multitenancy/wallet/{wallet_id}/remove                 | :white_check_mark: |
| POST   | /multitenancy/wallet/{wallet_id}/token                  | :white_check_mark: |
| GET    | /multitenancy/wallets                                   | :white_check_mark: |
|        | **out-of-band**                                         |                    |
| POST   | /out-of-band/create-invitation                          | :white_check_mark: |
| POST   | /out-of-band/receive-invitation                         | :white_check_mark: |
|        | **present-proof**                                       |                    |
| POST   | /present-proof/create-request                           | :white_check_mark: |
| GET    | /present-proof/records                                  | :white_check_mark: |
| GET    | /present-proof/records/{pres_ex_id}                     | :white_check_mark: |
| DELETE | /present-proof/records/{pres_ex_id}                     | :white_check_mark: |
| GET    | /present-proof/records/{pres_ex_id}/credentials         | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/problem-report      | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/send-presentation   | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/send-request        | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/verify-presentation | :white_check_mark: |
| POST   | /present-proof/send-proposal                            | :white_check_mark: |
| POST   | /present-proof/send-request                             | :white_check_mark: |
|        | **resolver**                                            |                    |
| GET    | /resolver/resolve/{did}                                 | :white_check_mark: |
|        | **revocation**                                          |                    |
| GET    | /revocation/active-registry/{cred_def_id}               | :white_check_mark: |
| POST   | /revocation/clear-pending-revocations                   | :white_check_mark: |
| POST   | /revocation/create-registry                             | :white_check_mark: |
| POST   | /revocation/publish-revocations                         | :white_check_mark: |
| GET    | /revocation/registries/created                          | :white_check_mark: |
| GET    | /revocation/registry/{rev_reg_id}                       | :white_check_mark: |
| PATCH  | /revocation/registry/{rev_reg_id}                       | :white_check_mark: |
| POST   | /revocation/revoke                                      | :white_check_mark: |
|        | **schema**                                              |                    |
| POST   | /schemas                                                | :white_check_mark: |
| GET    | /schemas/created                                        | :white_check_mark: |
| GET    | /schemas/{schema_id}                                    | :white_check_mark: |
|        | **server**                                              |                    |
| GET    | /status/config                                          | :white_check_mark: |
| GET    | /status/live                                            | :white_check_mark: |
| GET    | /status/ready                                           | :white_check_mark: |
|        | **trustping**                                           |                    |
| POST   | /connections/{conn_id}/send-ping                        | :white_check_mark: |
|        | **wallet**                                              |                    |
| GET    | /wallet/did                                             | :white_check_mark: |
| POST   | /wallet/did/create                                      | :white_check_mark: |
| GET    | /wallet/did/public                                      | :white_check_mark: |
| GET    | /wallet/get-did-endpoint                                | :white_check_mark: |
| POST   | /wallet/set-did-endpoint                                | :white_check_mark: |

## Client Examples

### Create the aca-py rest client

The default assumes you are running against a single wallet. In case of multi tenancy with base and sub wallets
the bearerToken needs to be set as well.

```java
AriesClient ac = AriesClient
        .builder()
        .url("https://myacapy.com:8031")
        .apiKey("secret") // optional - admin api key if set
        .bearerToken("123.456.789") // optional - jwt token - only when running in multi tennant mode
        .build();
```

### A Word on Credential POJO's

The library assumes credentials are flat Pojo's like:

```Java
@Data @NoArgsConstructor @Builder
@AttributeGroupName("referent") // the referent that should be matched in the proof request
public final class MyCredential {
   private String street;

   @AttributeName("e-mail")
   private String email;       // schema attribute name is e-mail

   @AttributeName(excluded = true)
   private String comment;     // internal field
}
```

How fields are serialised/deserialized can be changed by using the `@AttributeName` or `@AttributeGroupName` annotations.

### Create a connection

```java
ac.connectionsReceiveInvitation(
        ReceiveInvitationRequest.builder()
            .did(did)
            .label(label)
            .build(), 
        ConnectionReceiveInvitationFilter
            .builder()
            .alias("alias")
            .build())    
.ifPresent(connection -> {
    log.debug("{}", connection.getConnectionId());
});
```

### Issue a Credential

```Java
MyCredential myCredential = MyCredential
        .builder()
        .email("test@myexample.com")
        .build();
ac.issueCredentialSend(
        new V1CredentialProposalRequest(connectionId, credentialdefinitionId, myCredential));
```

### Present Proof Request

```Java
PresentProofRequest proofRequest = PresentProofRequestHelper.buildForEachAttribute(
    connectionId,
    MyCredential.class,
    ProofRestrictions.builder()
        .credentialDefinitionId(credentialDefinitionId)
        .build());
ac.presentProofSendRequest(proofRequest);
```

## Webhook/Websocket Handler Support

Webhook controller example

```java
@Controller
public class WebhookController {

    @Inject private EventHandler handler;

    @Post("/webhook/{topic}")
    public void ariesEvent(
            @PathVariable String topic,
            @Body String message) {
        handler.handleEvent(topic, message);
    }
}
```

Websocket client example

```java
import org.hyperledger.aries.config.GsonConfig;
import org.hyperledger.aries.webhook.WebsocketMessage;

public class AcyPyWebsocketClient extends WebSocketClient {
    
    private Gson gson = GsonConfig.defaultConfig();
    private EventHandler handler = new MyHandler();
    
    @OnMessage
    public void onMessage(String message) {
        WebsocketMessage msg = gson.fromJson(message, WebsocketMessage.class);
        handler.handleEvent(msg.getTopic(), msg.getPayload());
    }

    public static void main(String[] args) throws URISyntaxException {
        AcyPyWebsocketClient c = new AcyPyWebsocketClient(new URI(
                "ws://localhost:8031/ws"));
        c.connect();
    }
}
```

Your event handler can then extend the abstract EventHandler class which takes care of type conversion so that you can immediately implement your business logic.

```java
@Singleton
public class MyHandler extends EventHandler {
    @Override
    public void handleProof(PresentProofPresentation proof) {
        if (PresentationExchangeRole.VERIFIER.equals(proof.getRole())
                && PresentationExchangeState.VERIFIED.equals(proof.getState())) {    // received a validated proof
            MyCredential myCredential = proof.from(MyCredential.class);
            // If the presentation is based on multiple credentials this can be done multiple times
            // given that the POJO is annotated with @AttributeGroup e.g.
           MyOtherCredential otherCredential = proof.from(MyOtherCredential.class);
        }
    }
}
```
## Build Connectionless Proof Request

Connectionless proofs are more a thing of mobile wallets, because mostly they involve something that is presented to a human
like a barcode, but the java client supports this by providing models and builders.

A flow has the usually following steps:

1. The user is presented with a QRCode that contains an invitation URL like: https://myhost.com/url/1234
2. The server side HTTP handler of this URL responds with an HTTP.FOUND response that has the proof request encoded in the m parameter
3. The mobile wallet tries to match a stored credential, and then responds with a proof presentation if possible
3. The server side WebhookHandler waits for the proof and then triggers further actions

```java
@Get("/url/{requestId}")
public HttpResponse<Object> connectionLessProof(@QueryValue String requestId) {
    boolean matchingRequest = false; // TODO manage request states
    String proofRequestBase64 = ""; // TODO on how to build this see the example below
    if (matchingRequest) {
        return HttpResponse
                .status(HttpStatus.FOUND)
                .header("location", deploymentUri + "?m=" + proofRequestBase64;
    }
    return HttpResponse.notFound();
}
```
Proof Request Builder Example

```java
ProofRequestPresentationBuilder builder = new ProofRequestPresentationBuilder(ariesClient);

PresentProofRequest presentProofRequest = PresentProofRequestHelper.buildForEachAttribute(
        connectionId,
        List.of("name", "email"),
        ProofRestrictions
            .builder()
            .schemaId("WgWxqztrNooG92RXvxSTWv:2:schema_name:1.0")
            .build());

Optional<String> base64 = builder.buildRequest(presentProofRequest);
```