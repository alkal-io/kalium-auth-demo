package io.alkal.examples.key_rotation.trusted_authority;

import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSASigner;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Ziv Salzman
 * Created on 28-Apr-2019
 */
public class TokenIssuer {

    public static String issue(Map<String, String> claims, String privateKeyToSignWith) {

        Signer signer = RSASigner.newSHA256Signer(privateKeyToSignWith);
        JWT jwt = new JWT().setIssuer("io.alkal.demo")
                .setIssuedAt(ZonedDateTime.now(ZoneOffset.UTC))
                .setSubject("f1e33ab3-027f-47c5-bb07-8dd8ab37a2d3")
                .setExpiration(ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(60));

        claims.entrySet().forEach(claim -> jwt.addClaim(claim.getKey(), claim.getValue()));

        return  JWT.getEncoder().encode(jwt, signer);
    }
}
