package foundation.identity.keri.internal.event;

import foundation.identity.keri.api.event.EstablishmentEvent;
import foundation.identity.keri.api.event.KeyEventCoordinates;
import foundation.identity.keri.api.event.KeyCoordinates;
import foundation.identity.keri.api.event.KeyEventCoordinatesRecord;
import foundation.identity.keri.api.identifier.BasicIdentifier;

import static java.util.Objects.requireNonNull;

public record KeyCoordinatesRecord(KeyEventCoordinates establishmentEvent, int keyIndex) implements KeyCoordinates {

    public KeyCoordinatesRecord(KeyEventCoordinates establishmentEvent, int keyIndex) {
        if (keyIndex < 0) {
            throw new IllegalArgumentException("keyIndex must be >= 0");
        }

        this.establishmentEvent = requireNonNull(establishmentEvent, "establishmentEvent");
        this.keyIndex = keyIndex;
    }

    public static KeyCoordinatesRecord convert(KeyCoordinates coordinates) {
        if (coordinates instanceof KeyCoordinatesRecord ikc) {
            return ikc;
        }
        return new KeyCoordinatesRecord(
                coordinates.establishmentEvent(),
                coordinates.keyIndex()
        );
    }

    public static KeyCoordinatesRecord of(BasicIdentifier basicIdentifier) {
        var coordinates = KeyEventCoordinatesRecord.of(basicIdentifier);
        return new KeyCoordinatesRecord(coordinates, 0);
    }

    public static KeyCoordinatesRecord of(EstablishmentEvent establishmentEvent, int keyIndex) {
        var coordinates = KeyEventCoordinatesRecord.of(establishmentEvent);
        return new KeyCoordinatesRecord(coordinates, keyIndex);
    }
}
