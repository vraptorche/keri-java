package foundation.identity.keri.eventstorage.ipfs;

import foundation.identity.keri.KeyEventStore;
import foundation.identity.keri.api.KeyState;
import foundation.identity.keri.api.event.*;
import foundation.identity.keri.api.identifier.Identifier;

import java.util.Optional;
import java.util.OptionalLong;
import java.util.stream.Stream;

public class IpfsKeyEventStore implements KeyEventStore {
    @Override
    public void append(KeyEvent event) {

    }

    @Override
    public void append(AttachmentEvent event) {

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
        return null;
    }
}
