package foundation.identity.keri.controller.spec;

import foundation.identity.keri.SigningThresholds;
import foundation.identity.keri.api.event.SigningThreshold;
import foundation.identity.keri.crypto.SignatureOperations;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static foundation.identity.keri.SigningThresholds.weight;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class IdentifierSpecTests {

  SecureRandom deterministicRandom;
  KeyPair keyPair;
  PrivateKeySigner signer;
  KeyPair keyPair2;
  PrivateKeySigner signer2;

  @BeforeAll
   static void beforeClass() {
    // secp256k1 is considered "unsecure" so you have enable it like this:
    System.setProperty("jdk.sunec.disableNative", "false");
  }

  @BeforeEach
   void beforeEachTest() throws NoSuchAlgorithmException {
    // this makes the values of secureRandom deterministic
    this.deterministicRandom = SecureRandom.getInstance("SHA1PRNG");
    this.deterministicRandom.setSeed(new byte[]{0});

    this.keyPair = SignatureOperations.ED_25519.generateKeyPair();
    this.signer = new PrivateKeySigner(1, this.keyPair.getPrivate());

    this.keyPair2 = SignatureOperations.ED_25519.generateKeyPair();
    this.signer2 = new PrivateKeySigner(2, this.keyPair2.getPrivate());

  }

  @Test
   void test__builder__signingThreshold__int() {
    var spec = IdentifierSpec.builder()
        .key(this.keyPair.getPublic())
        .signer(this.signer)
        .signingThreshold(1)
        .build();

    assertTrue( spec.signingThreshold() instanceof SigningThreshold.Unweighted,"type");
    assertEquals(1, ((SigningThreshold.Unweighted) spec.signingThreshold()).threshold());
  }

  @Test
   void test__builder__signingThreshold__unweighted() {
    var spec = IdentifierSpec.builder()
        .key(this.keyPair.getPublic())
        .signer(this.signer)
        .signingThreshold(SigningThresholds.unweighted(1))
        .build();

    assertTrue(spec.signingThreshold() instanceof SigningThreshold.Unweighted, "type");
    assertEquals(1, ((SigningThreshold.Unweighted) spec.signingThreshold()).threshold());
  }

  @Test
   void test__builder__signingThreshold__weighted() {
    var spec = IdentifierSpec.builder()
        .key(this.keyPair.getPublic())
        .key(this.keyPair2.getPublic())
        .signer(this.signer)
        .signingThreshold(SigningThresholds.weighted("1", "2"))
        .build();

    assertTrue(spec.signingThreshold() instanceof SigningThreshold.Weighted, "type");
    var weights = ((SigningThreshold.Weighted) spec.signingThreshold()).weights();
    assertEquals(weight(1), weights[0][0]);
    assertEquals(weight(2), weights[0][1]);
  }

}
