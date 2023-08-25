package foundation.identity.keri.internal.event;

import foundation.identity.keri.api.event.SigningThreshold.Unweighted;

import java.util.Objects;

public record ImmutableUnweightedSigningThreshold(
        int threshold
) implements Unweighted {
}
