package foundation.identity.keri.internal.event;

import foundation.identity.keri.api.Version;
import foundation.identity.keri.api.event.Format;
import foundation.identity.keri.api.identifier.Identifier;

public record KeyEventDetails(
        Version version,
        Format format,
        Identifier identifier,
        long sequenceNumber) {
}
