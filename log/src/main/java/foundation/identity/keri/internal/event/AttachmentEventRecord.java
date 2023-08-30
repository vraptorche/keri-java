package foundation.identity.keri.internal.event;

import foundation.identity.keri.api.event.AttachmentEvent;
import foundation.identity.keri.api.event.KeyEventCoordinates;
import foundation.identity.keri.crypto.Signature;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public record AttachmentEventRecord(
        KeyEventCoordinates coordinates,
        Map<Integer, Signature> signatures,
        Map<KeyEventCoordinates, Map<Integer, Signature>> receipts,
        Map<Integer, Signature> endorsements
) implements AttachmentEvent {
    public AttachmentEventRecord {

        requireNonNull(coordinates, "coordinates");
        signatures = Map.copyOf(requireNonNull(signatures, "signatures"));
        receipts = copyOfReceipts(requireNonNull(receipts, "receipts"));
        endorsements = Map.copyOf(requireNonNull(endorsements, "endorsements"));
    }

    private static Map<KeyEventCoordinates, Map<Integer, Signature>> copyOfReceipts(
            Map<KeyEventCoordinates, Map<Integer, Signature>> otherReceipts) {
        return otherReceipts.entrySet()
                .stream()
                .map(e -> Map.entry(e.getKey(), Map.copyOf(e.getValue())))
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Map<Integer, Signature> authentication() {
        return this.signatures;
    }
}
