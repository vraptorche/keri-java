package foundation.identity.keri.crypto;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(ReplaceUnderscores.class)
class Blake3OperationsTest {

    @Test
    void digest_32() {
        Digest digest = Blake3Operations.BLAKE3_256.digest("bogus".getBytes(StandardCharsets.UTF_8));
        assertNotNull(digest);
        assertEquals(32, digest.bytes().length);
    }

    @Test
    void digest_64() {
        Digest digest = Blake3Operations.BLAKE3_512.digest("bogus".getBytes(StandardCharsets.UTF_8));
        assertNotNull(digest);
        assertEquals(64, digest.bytes().length);
    }

    @Test
    void invalid_size_throws() {
        assertThrows(IllegalArgumentException.class, () -> new Blake3Operations(24));
    }
}