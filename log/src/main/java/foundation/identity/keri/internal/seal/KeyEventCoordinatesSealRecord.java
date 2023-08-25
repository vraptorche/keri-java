package foundation.identity.keri.internal.seal;

import foundation.identity.keri.api.event.KeyEventCoordinates;
import foundation.identity.keri.api.seal.KeyEventCoordinatesSeal;

public record KeyEventCoordinatesSealRecord(KeyEventCoordinates event) implements KeyEventCoordinatesSeal {


}
