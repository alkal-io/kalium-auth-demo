package io.alkal.examples.key_rotation.secured_service;

import io.alkal.demos.key_rotations.models.PublicKey;
import io.alkal.kalium.Kalium;
import io.alkal.kalium.exceptions.KaliumBuilderException;
import io.alkal.kalium.exceptions.KaliumException;
import io.alkal.kalium.kafka.KaliumKafkaQueueAdapter;
import io.fusionauth.jwt.InvalidJWTException;
import io.fusionauth.jwt.InvalidJWTSignatureException;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.rsa.RSAVerifier;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ziv Salzman
 * Created on 11-May-2019
 */
public class AuthService {

    private AtomicReference<String> currentKey = new AtomicReference<>();
    private AtomicReference<String> previousKey = new AtomicReference<>();

    public void init() {
        try {
            Kalium kalium = Kalium.Builder().setQueueAdapter(new KaliumKafkaQueueAdapter("localhost:9092")).build();

            kalium.on(PublicKey.class, publicKey -> {
                System.out.println("New Public Key arrived:");
                System.out.println(publicKey.getValue());
                previousKey.set(currentKey.get());
                currentKey.set(publicKey.getValue());
            });
            kalium.start();
        } catch (KaliumException | KaliumBuilderException e) {
            //handle exception
        }
    }

    public boolean isTokenAllows(String claim, String encodedJWT) {
        JWT jwt;
        try {
            jwt = verifyAndDecodeJwt(encodedJWT);
        } catch (InvalidJWTSignatureException e) {
            return false;
        }
        return jwt.getOtherClaims().containsKey(claim);
    }

    private JWT verifyAndDecodeJwt(String encodedJWT) throws InvalidJWTSignatureException {
        try {
            //First try with current public key.
            Verifier verifier = RSAVerifier.newVerifier(currentKey.get());
            return JWT.getDecoder().decode(encodedJWT, verifier);
        } catch (InvalidJWTSignatureException e) {
            //Then try with previous public key
            Verifier verifier = RSAVerifier.newVerifier(previousKey.get());
            return JWT.getDecoder().decode(encodedJWT, verifier);
        } catch (InvalidJWTException e) {
            throw new InvalidJWTSignatureException();
        }
    }


}
