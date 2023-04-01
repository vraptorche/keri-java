package foundation.identity.keri.crypto;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;

import static foundation.identity.keri.crypto.EdDSAOperations.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EdDSAOperationsTest {

  @Test
  void generateKeyPair() {
    final KeyPair keyPair = ED_25519.generateKeyPair();
    final PublicKey publicKey = keyPair.getPublic();
    assertNotNull(publicKey);
  }
}