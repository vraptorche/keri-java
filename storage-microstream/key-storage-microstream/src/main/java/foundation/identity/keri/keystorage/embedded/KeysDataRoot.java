package foundation.identity.keri.keystorage.embedded;

import foundation.identity.keri.internal.event.KeyCoordinatesRecord;

import java.security.KeyPair;
import java.util.Map;

public record KeysDataRoot(
        Map<KeyCoordinatesRecord, KeyPair> keys,
        Map<KeyCoordinatesRecord, KeyPair> nextKeys
) {
}
