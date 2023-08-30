package foundation.identity.keri.controller.spec;

import foundation.identity.keri.SigningThresholds;
import foundation.identity.keri.api.event.KeyConfigurationDigest;
import foundation.identity.keri.api.event.SigningThreshold;
import foundation.identity.keri.crypto.Blake3Operations;
import foundation.identity.keri.crypto.Digest;
import foundation.identity.keri.crypto.SignatureOperations;
import foundation.identity.keri.internal.event.ImmutableKeyConfigurationDigest;
import org.junit.jupiter.api.*;


import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static foundation.identity.keri.SigningThresholds.weight;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DisplayNameGenerator.*;

@DisplayNameGeneration(ReplaceUnderscores.class)
class IdentifierSpecTests {

    SecureRandom deterministicRandom;
    KeyPair keyPair;
    PrivateKeySigner signer;
    KeyPair keyPair2;
    PrivateKeySigner signer2;

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

        assertTrue(spec.signingThreshold() instanceof SigningThreshold.Unweighted, "type");
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
    void test_builder_signingThreshold_weighted() {
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

    @Test
    void name() {
        KeyPair nextKeyPair1 = SignatureOperations.ED_25519.generateKeyPair();
        KeyPair nextKeyPair2 = SignatureOperations.ED_25519.generateKeyPair();

        byte[] bytes1 = nextKeyPair1.getPublic().getEncoded();
        Digest digest = Blake3Operations.BLAKE3_512.digest(bytes1);
        KeyConfigurationDigest nextKeysDigest = new ImmutableKeyConfigurationDigest(digest);
        var spec = IdentifierSpec.builder()
                .key(this.keyPair.getPublic())
                .key(this.keyPair2.getPublic())
                .nextKeys(nextKeysDigest)
                .signer(this.signer)
                .signingThreshold(SigningThresholds.weighted("1", "2"))
                .build();
    }
}
