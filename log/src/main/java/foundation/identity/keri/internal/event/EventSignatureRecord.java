package foundation.identity.keri.internal.event;

import foundation.identity.keri.api.event.EventSignature;
import foundation.identity.keri.api.event.KeyEventCoordinates;
import foundation.identity.keri.crypto.Signature;

import java.util.Map;

public record EventSignatureRecord(
        KeyEventCoordinates event,
        KeyEventCoordinates keyEstablishmentEvent,
        Map<Integer, Signature> signatures
) implements EventSignature {

    public EventSignatureRecord(
            KeyEventCoordinates event,
            KeyEventCoordinates keyEstablishmentEvent,
            Map<Integer, Signature> signatures) {
        this.event = event;
        this.keyEstablishmentEvent = keyEstablishmentEvent;
        this.signatures = Map.copyOf(signatures);
    }
}
