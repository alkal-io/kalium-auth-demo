package io.alkal.examples.key_rotation.trusted_authority;

import io.alkal.demos.key_rotations.models.PublicKey;
import io.alkal.kalium.Kalium;
import io.fusionauth.pem.domain.PEM;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ziv Salzman
 * Created on 27-Apr-2019
 */
public class KeyRotator {

    private Kalium kalium;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private AtomicReference<String> privateKey = new AtomicReference<>();
    private AtomicReference<String> publicKey = new AtomicReference<>();

    public KeyRotator(Kalium kalium) {
        this.kalium = kalium;
    }


    public void start() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                KeyPair keyPair = kpg.genKeyPair();
                privateKey.set(PEM.encode(keyPair.getPrivate()));
                publicKey.set(PEM.encode(keyPair.getPublic()));
                System.out.println("New Public Key:");
                System.out.println(publicKey.get());
                kalium.post(new PublicKey("SHA256-RSA", publicKey.get()));
            }
        }, 0l, 10, TimeUnit.MINUTES);
    }


    public String getPrivateKey() {
        return privateKey.get();
    }

    public String getPublicKey() {
        return publicKey.get();
    }
}
