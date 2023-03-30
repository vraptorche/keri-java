package foundation.identity.keri.eventstorage.embedded;

import foundation.identity.keri.api.event.KeyEventCoordinates;

public record ReceiptKey(
        KeyEventCoordinates event,
        KeyEventCoordinates signer
) {
}
