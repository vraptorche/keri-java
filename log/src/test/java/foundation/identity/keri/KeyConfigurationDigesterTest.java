package foundation.identity.keri;


import foundation.identity.keri.api.event.KeyConfigurationDigest;
import foundation.identity.keri.crypto.SignatureOperations;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.List;

import static foundation.identity.keri.KeyConfigurationDigester.digest;
import static foundation.identity.keri.KeyConfigurationDigester.signingThresholdRepresentation;
import static foundation.identity.keri.SigningThresholds.*;
import static foundation.identity.keri.crypto.StandardDigestAlgorithms.BLAKE3_512;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyConfigurationDigesterTest {

    @Test
    void test__signingThresholdRepresentation__unweighted() {
        assertArrayEquals(
                "1".getBytes(UTF_8),
                signingThresholdRepresentation(unweighted(1))
        );

        assertArrayEquals(
                Hex.hexNoPad(16).getBytes(UTF_8),
                signingThresholdRepresentation(unweighted(16))
        );

    }

    @Test
    void test_signingThresholdRepresentation_weighted() {
        assertArrayEquals(
                "1".getBytes(UTF_8),
                signingThresholdRepresentation(weighted("1"))
        );

        assertArrayEquals(
                "1,2,3".getBytes(UTF_8),
                signingThresholdRepresentation(weighted("1", "2", "3"))
        );

        assertArrayEquals(
                "1,2,3&4,5,6".getBytes(UTF_8),
                signingThresholdRepresentation(weighted(group("1", "2", "3"), group("4", "5", "6")))
        );

        assertArrayEquals(
                "1/2,1/3,1/4".getBytes(UTF_8),
                signingThresholdRepresentation(weighted("1/2", "1/3", "1/4"))
        );

        assertArrayEquals(
                "1,1/2,1/3&1,1/4,1/5,1/6".getBytes(UTF_8),
                signingThresholdRepresentation(
                        weighted(
                                group("1", "1/2", "1/3"),
                                group("1", "1/4", "1/5", "1/6")))
        );

    }

    @Test
    void test_digest() {
        KeyPair keyPair = SignatureOperations.ED_25519.generateKeyPair();
        KeyPair keyPair2 = SignatureOperations.ED_25519.generateKeyPair();
        List<PublicKey> nextKeys = List.of(
                keyPair.getPublic(),
                keyPair2.getPublic()
        );
        KeyConfigurationDigest digest = digest(unweighted(1), nextKeys, BLAKE3_512);
        assertEquals(BLAKE3_512, digest.algorithm());
    }
}
