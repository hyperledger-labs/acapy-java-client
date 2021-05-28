# ACA-PY Java Client Library

Convenience library based on okhttp and gson to interact with aries cloud agent python (aca-py) instances.  
It is currently work in progress and not all endpoints of the agent are present in the client.

## Version Compatibility

| Client Version | ACA-PY Version |
|----------------|----------------|
| \>= 0.15.0     | 0.6.0        |

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

## Create the aca-py rest client

The default assumes you are running against a single wallet. In case of multi tenancy with base and sub wallets
the bearerToken needs to be set as well.

```java
AriesClient ac = AriesClient
        .builder()
        .url("https://example.com")
        .apiKey("secret") // optional admin api key
        .bearerToken("123.456.789") // optional jwt token - only when running in multi tennant mode
        .build();
```

## A Word on Credential Definitions

The library assumes credentials, and their related credential definitions are flat Pojo's like:

```Java
@Data @NoArgsConstructor
public final class MyCredentialDefinition {
    private String street;
    
    @AttributeName("e-mail")
    private String email;       // schema attribute name is e-mail
    
    @AttributeName(excluded = true)
    private String comment;     // internal field
}
```

How fields are serialised/deserialized can be changed by using the @AttributeName annotation.

### Create a connection

```java
ac.connectionsReceiveInvitation(
        ReceiveInvitationRequest.builder()
        .did(did)
        .label(label)
        .build(), "alias")    
.ifPresent(connection -> {
    log.debug("{}", connection.getConnectionId());
});
```

### Issue a Credential

```Java
MyCredentialDefinition myCredentialDefinition = new MyCredentialDefinition("test@myexample.com")
ac.issueCredentialSend(new IssueCredentialSend(connectionId, credentialdefinitionId, myCredentialDefinition));
```

### Present Proof Request

```Java
PresentProofRequest proofRequest = PresentProofRequestHelper.buildForEachAttribute(
    connectionId,
    MySchemaPojo.class,
    ProofRestrictions.builder()
        .credentialDefinitionId(credentialDefinitionId)
        .build());
ac.presentProofSendRequest(PresentProofRequest.build(proofRequest));
```

## Webhook Handler

Assume you have a rest controller like this to handle aca-py webhook calls

```java
@Controller
public class WebhookController {

    @Inject private EventHandler handler;

    @Post("/topic/{eventType}")
    public void ariesEvent(
            @PathVariable String eventType,
            @Body String eventBody) {
        handler.handleEvent(eventType, eventBody);
    }
}
```

Your event handler implementation can then extend the abstract EventHandler class which takes care of type conversion so that you can immediately implement your business logic.

```java
@Singleton
public class MyHandler extends EventHandler {
    @Override
    public void handleProof(PresentProofPresentation proof) {
        if (proof.isVerified() && "verifier".equals(proof.getRole())) {     // received a validated proof
            MyCredentialDefinition myCredentialDefinition = proof.from(MyCredentialDefinition.class);
            //
        }
    }
}
```
## Build Connectionless Proof Request

Connectionless proofs are more a thing of mobile wallets, because mostly they involve something that is presented to a human
like a barcode, but the java client supports this by providing models and builders.

A flow has the usually following steps:

1. The user is presented with a QRCode that contains an invite URL like: https://myhost.com/url/1234
2. The server side HTTP handler of this URL responds with a HTTP.FOUND response that has the proof request encoded in the m parameter
3. The mobile wallet tries to match a credential, and then responds with a proof if possible
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