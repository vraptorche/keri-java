package foundation.identity.keri.internal.event;

import foundation.identity.keri.KeyEvents;
import foundation.identity.keri.api.Version;
import foundation.identity.keri.api.event.Format;
import foundation.identity.keri.api.event.KeyEvent;
import foundation.identity.keri.api.event.KeyEventCoordinates;
import foundation.identity.keri.api.event.KeyEventCoordinatesRecord;
import foundation.identity.keri.api.identifier.Identifier;
import foundation.identity.keri.crypto.Signature;

import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

public abstract class AbstractImmutableKeyEvent implements KeyEvent {

    private final KeyEventDetails eventDetails;
    private final byte[] bytes;
    private final KeyEventCoordinates previous;
    private final Map<Integer, Signature> signatures;
    private final Map<Integer, Signature> receipts;
    private final Map<KeyEventCoordinates, Map<Integer, Signature>> otherReceipts;

    // for lazily computing the event's coordinates
    private Supplier<KeyEventCoordinates> coordinates = () -> {
        // with multiple threads this might be ran multiple times concurrently, and that's ok
        var keyEventCoordinates = KeyEventCoordinatesRecord.of(this);
        this.coordinates = () -> keyEventCoordinates;
        return keyEventCoordinates;
    };

    public AbstractImmutableKeyEvent(
            KeyEventDetails eventDetails, KeyEventCoordinates previous,
            byte[] bytes,
            Map<Integer, Signature> signatures,
            Map<Integer, Signature> receipts,
            Map<KeyEventCoordinates, Map<Integer, Signature>> otherReceipts) {
        this.eventDetails = eventDetails;
        this.bytes = requireNonNull(bytes, "bytes");
        this.previous = requireNonNull(previous, "previous");
        this.signatures = Map.copyOf(requireNonNull(signatures, "signatures"));
        this.receipts = Map.copyOf(requireNonNull(receipts, "receipts"));
        this.otherReceipts = Map.copyOf(requireNonNull(otherReceipts, "otherReceipts"));
    }

    @Override
    public Version version() {
        return this.eventDetails.version();
    }

    @Override
    public Format format() {
        return this.eventDetails.format();
    }

    @Override
    public byte[] bytes() {
        return this.bytes.clone();
    }

    @Override
    public Identifier identifier() {
        return this.eventDetails.identifier();
    }

    @Override
    public long sequenceNumber() {
        return this.eventDetails.sequenceNumber();
    }

    @Override
    public KeyEventCoordinates coordinates() {
        return this.coordinates.get();
    }

    @Override
    public KeyEventCoordinates previous() {
        return this.previous;
    }

    @Override
    public Map<Integer, Signature> authentication() {
        return this.signatures;
    }

    @Override
    public Map<Integer, Signature> endorsements() {
        return this.receipts;
    }

    @Override
    public Map<KeyEventCoordinates, Map<Integer, Signature>> receipts() {
        return this.otherReceipts;
    }

    @Override
    public String toString() {
        return KeyEvents.toString(this);
    }

}
