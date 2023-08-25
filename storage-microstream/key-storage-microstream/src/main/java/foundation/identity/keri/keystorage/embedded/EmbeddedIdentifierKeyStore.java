package foundation.identity.keri.keystorage.embedded;

import foundation.identity.keri.IdentifierKeyStore;
import foundation.identity.keri.api.event.KeyCoordinates;
import foundation.identity.keri.internal.event.KeyCoordinatesRecord;
import one.microstream.storage.types.StorageManager;

import java.security.KeyPair;
import java.util.Optional;

public class EmbeddedIdentifierKeyStore implements IdentifierKeyStore {

    private final StorageManager storageManager;

    public EmbeddedIdentifierKeyStore(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public void storeKey(KeyCoordinates keyCoordinates, KeyPair keyPair) {
        // TODO: 3/30/2023 Implement
        KeysDataRoot dataRoot = (KeysDataRoot) storageManager.root();
        dataRoot.keys().put(KeyCoordinatesRecord.convert(keyCoordinates), keyPair);
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
        // TODO: 3/30/2023 Implement
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
