package foundation.identity.keri.api.event;

import foundation.identity.keri.api.crypto.Signature;

import java.util.Map;

public interface EventSignature {

  KeyEventCoordinates event();

  KeyEventCoordinates keyEstablishmentEvent();

  Map<Integer, Signature> signatures();

}
