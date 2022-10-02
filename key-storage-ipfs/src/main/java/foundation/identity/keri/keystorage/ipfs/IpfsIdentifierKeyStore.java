package foundation.identity.keri.keystorage.ipfs;

import foundation.identity.keri.IdentifierKeyStore;
import foundation.identity.keri.api.event.KeyCoordinates;

import java.security.KeyPair;
import java.util.Optional;

public class IpfsIdentifierKeyStore implements IdentifierKeyStore {
    @Override
    public void storeKey(KeyCoordinates keyCoordinates, KeyPair keyPair) {

    }

    @Override
    public Optional<KeyPair> getKey(KeyCoordinates keyCoordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<KeyPair> removeKey(KeyCoordinates keyCoordinates) {
        return Optional.empty();
    }

    @Override
    public void storeNextKey(KeyCoordinates keyCoordinates, KeyPair keyPair) {

    }

    @Override
    public Optional<KeyPair> getNextKey(KeyCoordinates keyCoordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<KeyPair> removeNextKey(KeyCoordinates keyCoordinates) {
        return Optional.empty();
    }
}
