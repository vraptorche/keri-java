package foundation.identity.keri.controller.spec;

import foundation.identity.keri.crypto.EdDSAOperations;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

import static foundation.identity.keri.crypto.StandardSignatureAlgorithms.ED_25519;
import static org.junit.jupiter.api.Assertions.*;

class PrivateKeySignerTest {

    @Test
    void test_sign() {
        KeyPair keyPair = EdDSAOperations.ED_25519.generateKeyPair();
        PrivateKeySigner keySigner = new PrivateKeySigner(0, keyPair.getPrivate());
        assertEquals(keySigner.algorithm(), ED_25519);
        assertEquals(0, keySigner.keyIndex());
    }
}