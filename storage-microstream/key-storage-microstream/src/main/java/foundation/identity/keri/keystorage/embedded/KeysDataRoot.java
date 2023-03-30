package foundation.identity.keri.keystorage.embedded;

import foundation.identity.keri.internal.event.ImmutableKeyCoordinates;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public record KeysDataRoot(
        Map<ImmutableKeyCoordinates, KeyPair> keys,
        Map<ImmutableKeyCoordinates, KeyPair> nextKeys
) {
}
