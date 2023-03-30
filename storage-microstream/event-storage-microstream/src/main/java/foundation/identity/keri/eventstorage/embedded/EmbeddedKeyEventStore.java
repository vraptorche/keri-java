package foundation.identity.keri.eventstorage.embedded;

import foundation.identity.keri.KeyEventStore;
import foundation.identity.keri.api.KeyState;
import foundation.identity.keri.api.event.*;
import foundation.identity.keri.api.identifier.Identifier;
import one.microstream.storage.types.StorageManager;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

public class EmbeddedKeyEventStore implements KeyEventStore {

    private final StorageManager storageManager;

    public EmbeddedKeyEventStore(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Override
    public void append(KeyEvent event) {
        // TODO: 3/30/2023 Implement
    }

    @Override
    public void append(AttachmentEvent event) {
        // TODO: 3/30/2023 Implement
    }

    @Override
    public Optional<SealingEvent> getKeyEvent(DelegatingEventCoordinates coordinates) {
        return Optional.empty();
    }

    @Override
    public Optional<KeyEvent> getKeyEvent(KeyEventCoordinates coordinates) {
        return Optional.empty();
    }

    @Override
    public Stream<KeyEvent> streamKeyEvents(Identifier identifier) {
        return null;
    }

    @Override
    public Stream<KeyEvent> streamKeyEvents(Identifier identifier, long from) {
        return null;
    }

    @Override
    public Optional<KeyState> getKeyState(Identifier identifier) {
        return Optional.empty();
    }

    @Override
    public Optional<KeyState> getKeyState(KeyEventCoordinates previous) {
        return Optional.empty();
    }

    @Override
    public OptionalLong findLatestReceipt(Identifier forIdentifier, Identifier byIdentifier) {
        return OptionalLong.empty();
    }
}
