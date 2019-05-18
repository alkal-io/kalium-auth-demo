# kalium-auth-demo
Demonstrate using Kalium to rotate public keys in asymmetrical JWTs based auth in a micro-services ecosystem

This demo consist of two microservices:
1. A trusted-authority - this service is responsible for signing JWT tokens that are used to authenticate and authorize calls in all other services.
2. A secured-service - an example of a service that uses asymmetrically signed JWT to authenticate and authorize external calls.


The secured-service, for demo purposes, provides exposes a REST API method, ```GET /securedHelloWorld```. The service expects to get a signed JWT with every API call. If the JWT is valid, the service returns ```"Hello World"```. However, if it's not, the service returns ```"Invalid token :("```. 

Since asymmetrically signed JWTs are in use, the secured-service needs to know what is the public key of the signer which signed the tokens.

The trusted-authority is the one responsible for signing the JWTs. It exposes a REST API for signing tokens, ```POST /issueSignedToken```. The API expects a list of claims that are added to the signed JWT. In return, the API response with a base64 encoded JWT token, signed with RSA SHA256 key.

Here's an example of a request payload:
```
{
    "greet" : true
}
```


 
