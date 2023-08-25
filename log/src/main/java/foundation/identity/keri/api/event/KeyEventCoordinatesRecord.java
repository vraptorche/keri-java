package foundation.identity.keri.api.event;

import foundation.identity.keri.api.identifier.BasicIdentifier;
import foundation.identity.keri.api.identifier.Identifier;
import foundation.identity.keri.crypto.Digest;
import foundation.identity.keri.crypto.DigestAlgorithm;
import foundation.identity.keri.crypto.DigestOperations;
import foundation.identity.keri.crypto.StandardDigestAlgorithms;

import static java.util.Objects.requireNonNull;

public record KeyEventCoordinatesRecord(
        Identifier identifier,
        long sequenceNumber,
        Digest digest
) implements KeyEventCoordinates {
    public KeyEventCoordinatesRecord(Identifier identifier, long sequenceNumber, Digest digest) {
        if (sequenceNumber < 0) {
            throw new IllegalArgumentException("sequenceNumber must be >= 0");
        }

        this.identifier = requireNonNull(identifier, "identifier");
        this.sequenceNumber = sequenceNumber;

        if ((!(identifier instanceof BasicIdentifier) || sequenceNumber != 0)
                && Digest.NONE.equals(digest)) {
            // Digest isn't required for BasicIdentifiers or for inception events
            throw new IllegalArgumentException("digest is required");
        }

        this.digest = requireNonNull(digest, "digest");
    }

    public static KeyEventCoordinatesRecord convert(KeyEventCoordinates coordinates) {
        requireNonNull(coordinates, "coordinates");
        if (coordinates instanceof KeyEventCoordinatesRecord ikec) {
            return ikec;
        }
        return new KeyEventCoordinatesRecord(
                coordinates.identifier(),
                coordinates.sequenceNumber(),
                coordinates.digest()
        );
    }

    public static KeyEventCoordinatesRecord of(KeyEvent event) {
        requireNonNull(event, "event");
        var algorithm = event.previous().equals(KeyEventCoordinates.NONE)
                ? StandardDigestAlgorithms.DEFAULT
                : event.previous().digest().algorithm();

        return of(event, algorithm);
    }

    public static KeyEventCoordinatesRecord of(KeyEvent event, DigestAlgorithm algorithm) {
        requireNonNull(event, "event");
        requireNonNull(algorithm, "algorithm");
        return of(event, DigestOperations.lookup(algorithm));
    }

    private static KeyEventCoordinatesRecord of(KeyEvent event, DigestOperations ops) {
        var digest = ops.digest(event.bytes());
        return of(event, digest);
    }

    public static KeyEventCoordinatesRecord of(KeyEvent event, Digest digest) {
        return new KeyEventCoordinatesRecord(event.identifier(), event.sequenceNumber(), digest);
    }

    public static KeyEventCoordinatesRecord of(KeyEventCoordinates event, Digest digest) {
        return new KeyEventCoordinatesRecord(event.identifier(), event.sequenceNumber(), digest);
    }

    public static KeyEventCoordinatesRecord of(BasicIdentifier identifier) {
        return new KeyEventCoordinatesRecord(identifier, 0, Digest.NONE);
    }
}
