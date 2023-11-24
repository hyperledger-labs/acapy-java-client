# ACA-PY Java Client Library

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)
[![CI/CD](https://github.com/hyperledger-labs/acapy-java-client/workflows/CI/CD/badge.svg)](https://github.com/hyperledger-labs/acapy-java-client/actions?query=workflow%3ACI%2FCD+branch%3Amain)
![Maven Central](https://img.shields.io/maven-central/v/network.idu.acapy/aries-client-python)

Convenience library based on okhttp and gson to interact with [aries cloud agent python](https://github.com/hyperledger/aries-cloudagent-python) (aca-py) instances.

## Use it in your project

```xml
<dependency>
   <groupId>network.idu.acapy</groupId>
   <artifactId>aries-client-python</artifactId>
   <version>0.10.0</version>
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

| Client Version | ACA-PY Version |
|----------------|----------------|
| 0.10.0         | 0.10.x         |
| 0.8.0          | 0.8.0          |
| 0.7.0          | 0.7.0          |
| 0.7.6          | 0.7.1, 0.7.2   |
| \>= 0.7.18     | 0.7.3          |
| \>= 0.7.25     | 0.7.4          |
| \>= 0.7.32     | 0.7.5          |

## Implemented Endpoints

| Method | Endpoint                                                     | Implemented        |
|--------|--------------------------------------------------------------|--------------------|
|        | **action-menu**                                              |                    |
| POST   | /action-menu/{conn_id}/close                                 | :white_check_mark: |
| POST   | /action-menu/{conn_id}/fetch                                 | :white_check_mark: |
| POST   | /action-menu/{conn_id}/perform                               | :white_check_mark: |
| POST   | /action-menu/{conn_id}/request                               | :white_check_mark: |
| POST   | /action-menu/{conn_id}/send-menu                             | :white_check_mark: |
|        | **basicmessage**                                             |                    |
| POST   | /connections/{conn_id}/send-message                          | :white_check_mark: |
|        | **connection**                                               |                    |
| GET    | /connections                                                 | :white_check_mark: |
| POST   | /connections/create-invitation                               | :white_check_mark: |
| POST   | /connections/create-static                                   | :white_check_mark: |
| POST   | /connections/receive-invitation                              | :white_check_mark: |
| GET    | /connections/{conn_id}}                                      | :white_check_mark: |
| DELETE | /connections/{conn_id}                                       | :white_check_mark: |
| POST   | /connections/{conn_id}/accept-invitation                     | :white_check_mark: |
| POST   | /connections/{conn_id}/accept-request                        | :white_check_mark: |
| GET    | /connections/{conn_id}/endpoints                             | :white_check_mark: |
| POST   | /connections/{conn_id}/establish-inbound/{ref_id}            | :white_check_mark: |
| GET    | /connections/{conn_id}/metadata                              | :white_check_mark: |
| POST   | /connections/{conn_id}/metadata                              | :white_check_mark: |
|        | **credential-definition**                                    |                    |
| POST   | /credential-definitions                                      | :white_check_mark: |
| GET    | /credential-definitions/created                              | :white_check_mark: |
| GET    | /credential-definitions/{cred_def_id}                        | :white_check_mark: |
| POST   | /credential-definitions/{cred_def_id}/write_record           | :white_check_mark: |
|        | **credentials**                                              |                    |
| GET    | /credentials/mime-types/{credential_id}                      | :white_check_mark: |
| GET    | /credentials/revoked/{credential_id}                         | :white_check_mark: |
| GET    | /credential/w3c/{credential_id}                              | :white_check_mark: |
| DELETE | /credential/w3c/{credential_id}                              | :white_check_mark: |
| GET    | /credential/{credential_id}                                  | :white_check_mark: |
| DELETE | /credential/{credential_id}                                  | :white_check_mark: |
| GET    | /credentials                                                 | :white_check_mark: |
| POST   | /credentials/w3c                                             | :white_check_mark: |
|        | **did-exchange**                                             |                    |
| POST   | /didexchange/create-request                                  | :white_check_mark: |
| POST   | /didexchange/receive-request                                 | :white_check_mark: |
| POST   | /didexchange/{conn_id}/accept-invitation                     | :white_check_mark: |
| POST   | /didexchange/{conn_id}/accept-request                        | :white_check_mark: |
| POST   | /didexchange/{conn_id}/reject                                | :white_check_mark: |
|        | **discover-features**                                        |                    |
| GET    | /discover-features/query                                     | :white_check_mark: |
| GET    | /discover-features/records                                   | :white_check_mark: |
|        | **discover-features v2.0**                                   |                    |
| GET    | /discover-features-2.0/queries                               | :white_check_mark: |
| GET    | /discover-features-2.0/records                               | :white_check_mark: |
|        | **endorse-transaction**                                      |                    |
| POST   | /transaction/{tran_id}/resend                                | :white_check_mark: |
| POST   | /transactions                                                | :white_check_mark: |
| POST   | /transactions/create-request                                 | :white_check_mark: |
| POST   | /transactions/{conn_id}/set-endorser-info                    | :white_check_mark: |
| POST   | /transactions/{conn_id}/set-endorser-role                    | :white_check_mark: |
| POST   | /transactions/{tran_id}                                      | :white_check_mark: |
| POST   | /transactions/{tran_id}/cancel                               | :white_check_mark: |
| POST   | /transactions/{tran_id}/endorse                              | :white_check_mark: |
| POST   | /transactions/{tran_id}/refuse                               | :white_check_mark: |
| POST   | /transactions/{tran_id}/write                                | :white_check_mark: |
|        | **introduction**                                             |                    |
| POST   | /connections/{conn_id}/start-introduction                    | :white_check_mark: |
|        | **issue-credential v1.0**                                    |                    |
| POST   | /issue-credential/create                                     | :white_check_mark: |
| POST   | /issue-credential/create-offer                               | :white_check_mark: |
| GET    | /issue-credential/records                                    | :white_check_mark: |
| GET    | /issue-credential/records/{cred_ex_id}                       | :white_check_mark: |
| DELETE | /issue-credential/records/{cred_ex_id}                       | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/issue                 | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/problem-report        | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/send-offer            | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/send-request          | :white_check_mark: |
| POST   | /issue-credential/records/{cred_ex_id}/store                 | :white_check_mark: |
| POST   | /issue-credential/send                                       | :white_check_mark: |
| POST   | /issue-credential/send-offer                                 | :white_check_mark: |
| POST   | /issue-credential/send-proposal                              | :white_check_mark: |
|        | **issue-credential v2.0**                                    |                    |
| POST   | /issue-credential-2.0/create                                 | :white_check_mark: |
| POST   | /issue-credential-2.0/create-offer                           | :white_check_mark: |
| GET    | /issue-credential-2.0/records                                | :white_check_mark: |
| GET    | /issue-credential-2.0/records/{cred_ex_id}                   | :white_check_mark: |
| DELETE | /issue-credential-2.0/records/{cred_ex_id}                   | :white_check_mark: |
| POST   | /issue-credential-2.0/records/{cred_ex_id}/issue             | :white_check_mark: |
| POST   | /issue-credential-2.0/records/{cred_ex_id}/problem-report    | :white_check_mark: |
| POST   | /issue-credential-2.0/records/{cred_ex_id}/send-offer        | :white_check_mark: |
| POST   | /issue-credential-2.0/records/{cred_ex_id}/send-request      | :white_check_mark: |
| POST   | /issue-credential-2.0/records/{cred_ex_id}/store             | :white_check_mark: |
| POST   | /issue-credential-2.0/send                                   | :white_check_mark: |
| POST   | /issue-credential-2.0/send-offer                             | :white_check_mark: |
| POST   | /issue-credential-2.0/send-proposal                          | :white_check_mark: |
| POST   | /issue-credential-2.0/send-request                           | :white_check_mark: |
|        | **jsonld**                                                   |                    |
| POST   | /jsonld/sign                                                 | :white_check_mark: |
| POST   | /jsonld/verify                                               | :white_check_mark: |
|        | **ledger**                                                   |                    |
| GET    | /ledger/config                                               | :white_check_mark: |
| GET    | /ledger/did-endpoint                                         | :white_check_mark: |
| GET    | /ledger/did-verkey                                           | :white_check_mark: |
| GET    | /ledger/get-nym-role                                         | :white_check_mark: |
| GET    | /ledger/get-write-ledger                                     | :white_check_mark: |
| GET    | /ledger/get-write-ledgers                                    | :white_check_mark: |
| POST   | /ledger/register-nym                                         | :white_check_mark: |
| PATCH  | /ledger/rotate-public-did-keypair                            | :white_check_mark: |
| GET    | /ledger/taa                                                  | :white_check_mark: |
| POST   | /ledger/taa/accept                                           | :white_check_mark: |
| POST   | /ledger/{ledger_id}/set-write-ledger                         | :white_check_mark: |
|        | **mediation**                                                |                    |
| GET    | /mediation/default-mediator                                  | :white_check_mark: |
| DELETE | /mediation/default-mediator                                  | :white_check_mark: |
| GET    | /mediation/keylists                                          | :white_check_mark: |
| POST   | /mediation/keylists/{mediation_id}/send-keylist-query        | :white_check_mark: |
| POST   | /mediation/keylists/{mediation_id}/send-keylist-update       | :white_check_mark: |
| POST   | /mediation/request/{conn_id}                                 | :white_check_mark: |
| GET    | /mediation/requests                                          | :white_check_mark: |
| GET    | /mediation/requests/{mediation_id}                           | :white_check_mark: |
| DELETE | /mediation/requests/{mediation_id}                           | :white_check_mark: |
| POST   | /mediation/requests/{mediation_id}/deny                      | :white_check_mark: |
| POST   | /mediation/requests/{mediation_id}/grant                     | :white_check_mark: |
| POST   | /mediation/update-keylist/{conn_id}                          | :white_check_mark: |
| PUT    | /mediation/{mediation_id}/default-mediator                   | :white_check_mark: |
|        | **multitenancy**                                             |                    |
| POST   | /multitenancy/wallet                                         | :white_check_mark: |
| GET    | /multitenancy/wallet/{wallet_id}                             | :white_check_mark: |
| PUT    | /multitenancy/wallet/{wallet_id}                             | :white_check_mark: |
| POST   | /multitenancy/wallet/{wallet_id}/remove                      | :white_check_mark: |
| POST   | /multitenancy/wallet/{wallet_id}/token                       | :white_check_mark: |
| GET    | /multitenancy/wallets                                        | :white_check_mark: |
|        | **out-of-band**                                              |                    |
| POST   | /out-of-band/create-invitation                               | :white_check_mark: |
| POST   | /out-of-band/receive-invitation                              | :white_check_mark: |
|        | **present-proof v1.0**                                       |                    |
| POST   | /present-proof/create-request                                | :white_check_mark: |
| GET    | /present-proof/records                                       | :white_check_mark: |
| GET    | /present-proof/records/{pres_ex_id}                          | :white_check_mark: |
| DELETE | /present-proof/records/{pres_ex_id}                          | :white_check_mark: |
| GET    | /present-proof/records/{pres_ex_id}/credentials              | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/problem-report           | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/send-presentation        | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/send-request             | :white_check_mark: |
| POST   | /present-proof/records/{pres_ex_id}/verify-presentation      | :white_check_mark: |
| POST   | /present-proof/send-proposal                                 | :white_check_mark: |
| POST   | /present-proof/send-request                                  | :white_check_mark: |
|        | **present-proof v2.0**                                       |                    |
| POST   | /present-proof-2.0/create-request                            | :white_check_mark: |
| GET    | /present-proof-2.0/records                                   | :white_check_mark: |
| GET    | /present-proof-2.0/records/{pres_ex_id}                      | :white_check_mark: |
| DELETE | /present-proof-2.0/records/{pres_ex_id}                      | :white_check_mark: |
| GET    | /present-proof-2.0/records/{pres_ex_id}/credentials          | :white_check_mark: |
| POST   | /present-proof-2.0/records/{pres_ex_id}/problem-report       | :white_check_mark: |
| POST   | /present-proof-2.0/records/{pres_ex_id}/send-presentation    | :white_check_mark: |
| POST   | /present-proof-2.0/records/{pres_ex_id}/send-request         | :white_check_mark: |
| POST   | /present-proof-2.0/records/{pres_ex_id}/verify-presentation  | :white_check_mark: |
| POST   | /present-proof-2.0/send-proposal                             | :white_check_mark: |
| POST   | /present-proof-2.0/send-request                              | :white_check_mark: |
|        | **resolver**                                                 |                    |
| GET    | /resolver/resolve/{did}                                      | :white_check_mark: |
|        | **revocation**                                               |                    |
| GET    | /revocation/active-registry/{cred_def_id}                    | :white_check_mark: |
| POST   | /revocation/active-registry/{cred_def_id}/rotate             | :white_check_mark: |
| POST   | /revocation/clear-pending-revocations                        | :white_check_mark: |
| POST   | /revocation/create-registry                                  | :white_check_mark: |
| GET    | /revocation/credential-record                                | :white_check_mark: |
| POST   | /revocation/publish-revocations                              | :white_check_mark: |
| GET    | /revocation/registries/created                               | :white_check_mark: |
| DELETE | /revocation/registry/delete-tails-file                       | :white_check_mark: |
| GET    | /revocation/registry/{rev_reg_id}                            | :white_check_mark: |
| PATCH  | /revocation/registry/{rev_reg_id}                            | :white_check_mark: |
| POST   | /revocation/registry/{rev_reg_id}/definition                 | :white_check_mark: |
| POST   | /revocation/registry/{rev_reg_id}/entry                      | :white_check_mark: |
| PUT    | /revocation/registry/{rev_reg_id}/fix-revocation-entry-state | :white_check_mark: |
| GET    | /revocation/registry/{rev_reg_id}/issued                     | :white_check_mark: |
| GET    | /revocation/registry/{rev_reg_id}/issued/details             | :white_check_mark: |
| GET    | /revocation/registry/{rev_reg_id}/issued/indy_recs           | :white_check_mark: |
| PATCH  | /revocation/registry/{rev_reg_id}/set-state                  | :white_check_mark: |
| PUT    | /revocation/registry/{rev_reg_id}/tails-file                 | :white_check_mark: |
| GET    | /revocation/registry/{rev_reg_id}/tails-file                 | :white_check_mark: |
| POST   | /revocation/revoke                                           | :white_check_mark: |
|        | **schema**                                                   |                    |
| POST   | /schemas                                                     | :white_check_mark: |
| GET    | /schemas/created                                             | :white_check_mark: |
| GET    | /schemas/{schema_id}                                         | :white_check_mark: |
| POST   | /schemas/{schema_id}/write_record                            | :white_check_mark: |
|        | **settings**                                                 |                    |
| PUT    | /settings                                                    | :white_check_mark: |
| GET    | /settings                                                    | :white_check_mark: |
|        | **trustping**                                                |                    |
| POST   | /connections/{conn_id}/send-ping                             | :white_check_mark: |
|        | **wallet**                                                   |                    |
| GET    | /wallet/did                                                  | :white_check_mark: |
| POST   | /wallet/did/create                                           | :white_check_mark: |
| PATCH  | /wallet/did/local/rotate-keypair                             | :white_check_mark: |
| GET    | /wallet/did/public                                           | :white_check_mark: |
| POST   | /wallet/did/public                                           | :white_check_mark: |
| GET    | /wallet/get-did-endpoint                                     | :white_check_mark: |
| POST   | /wallet/jwt/sign                                             | :white_check_mark: |
| POST   | /wallet/jwt/verify                                           | :white_check_mark: |
| POST   | /wallet/set-did-endpoint                                     | :white_check_mark: |
|        | **server**                                                   |                    |
| GET    | /plugins                                                     | :white_check_mark: |
| GET    | /shutdown                                                    | :white_check_mark: |
| GET    | /status                                                      | :white_check_mark: |
| GET    | /status/config                                               | :white_check_mark: |
| GET    | /status/live                                                 | :white_check_mark: |
| GET    | /status/ready                                                | :white_check_mark: |
| POST   | /status/reset                                                | :white_check_mark: |

## Client Examples

### Sending Requests: Create the aca-py Rest Client

The rest client is used to send requests against aca-py's admin rest endpoint.

Related aca-py config flags are: `--admin <host> <port>`, `--admin-api-key <api-key>`

The default assumes you are running against a single wallet. In case of multi tenancy with base and sub wallets
the bearerToken needs to be set as well.

Example aca-py config flags:

```shell
--admin 0.0.0.0 8031
--admin-api-key secret
```

Example client builder:

```java
AriesClient ac = AriesClient
        .builder()
        .url("http://localhost:8031") // optional - defaults to localhost:8031
        .apiKey("secret") // optional - admin api key if set
        .bearerToken("123.456.789") // optional - jwt token - only when running in multi tenant mode
        .build();
```

### Receiving Events: Webhook and Websocket Handler Support

With aca-py you have three options on how to receive status changes:
1. Poll the rest API - this is not recommended
2. Register a webhook URL
3. Connect to aca-py's websocket

#### Webhook

Related aca-py config flag: `--webhook-url <url#api_key>`

##### Single Tenant Controller Example

If running a single wallet and not in multi tenant mode.

Example aca-py config flag: `--webhook-url http://localhost:8080/webhook`

```java
@Controller
public class WebhookController {

   private EventHandler handler = new EventHandler.DefaultEventHandler();

   @Post("/webhook/topic/{topic}")
   public void handleWebhookEvent(
           @PathVariable String topic,
           @Body String payload) {
      handler.handleEvent(topic, payload);
   }
}
```

##### Multi Tenant Example

If running in multi tenant mode.

Example aca-py config flags:

```shell
--webhook-url http://localhost:8080/webhook
--multitenant
--jwt-secret 1234
--multitenant-admin
```

Example multi tenant webhook controller

```java
@Controller
public class WebhookController {

   private EventHandler handler = new TenantAwareEventHandler.DefaultTenantAwareEventHandler();

   @Post("/webhook/topic/{topic}")
   public void handleWebhookEvent(
           @PathVariable String topic,
           @Body String payload,
           HttpRequest request) {
      String walletId = request.getHeaders().get("x-wallet-id");
      handler.handleEvent(walletId, topic, payload);
   }
}
```

#### Websocket

If the admin api is enabled aca-py also supports a websocket endpoint under `ws(s)://<host>:<admin-port>/ws`

Example aca-py config flag: `--admin 0.0.0.0 8031`

To connect with the websocket you can use the `AriesWebSocketClient` like:

```java
@Factory
public class AriesSocketFactory {

   @Value("${acapy.ws.url}")
   private String url;
   @Value("${acapy.admin.apiKey}")
   private String apiKey;
   
   @Singleton
   @Bean(preDestroy = "closeWebsocket")
   public AriesWebSocketClient ariesWebSocketClient() {
      return AriesWebSocketClient
              .builder()
              .url(url) // optional - defaults to ws://localhost:8031/ws
              .apiKey(apiKey) // optional - admin api key if set
              .handler(new EventHandler.DefaultEventHandler()) // optional - your handler implementation
              // .bearerToken(bearer) // optional - jwt token - only when running in multi tenant mode
              .build();
   }
}
```

#### Writing Custom Event Handlers

To add your own event handler implementation to use in webhooks or in the websocket client, you can either extend or instantiate one of the following classes:

1. `EventHandler`
2. `TenantAwareEventHandler` 
3. `ReactiveEventHandler`

All classes take care of type conversion so that you can immediately start implementing your business logic.

#### Basic EventHandler

```java
@Singleton
public class MyHandler extends EventHandler {
    @Override
    public void handleProof(PresentationExchangeRecord proof) {
        if (proof.roleIsVerifierAndVerified()) {    // received a validated proof
            MyCredential myCredential = proof.from(MyCredential.class);
            // If the presentation is based on multiple credentials this can be done multiple times
            // given that the POJO is annotated with @AttributeGroup e.g.
            MyOtherCredential otherCredential = proof.from(MyOtherCredential.class);
        }
    }
}
```

#### Reactive Event Handler

As the websocket client already implements the EventHandler interface you can directly use it like:

```java
AriesWebSocketClient ws = AriesWebSocketClient.builder().build();

// do some stuff, create a connection, or receive invitation

// blocking wait 
ConnectionRecord active = ws.connection()
    .filter(ConnectionRecord::stateIsActive)
    .blockFirst(Duration.ofSeconds(5));
// none blocking
ws.connection()
    .filter(ConnectionRecord::stateIsActive)
    .subscribe(System.out::println);
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