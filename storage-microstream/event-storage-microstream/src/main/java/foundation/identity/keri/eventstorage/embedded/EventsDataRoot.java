package foundation.identity.keri.eventstorage.embedded;

import foundation.identity.keri.api.KeyState;
import foundation.identity.keri.api.event.KeyEvent;
import foundation.identity.keri.api.event.KeyEventCoordinates;
import foundation.identity.keri.crypto.Signature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventsDataRoot {

    private List<KeyEvent> events = new ArrayList<>();
    private Map<KeyEventCoordinates, KeyState> states = new HashMap<>();
    private Map<KeyEventCoordinates, Map<Integer, Signature>> authentications = new HashMap<>();
    private Map<KeyEventCoordinates, Map<Integer, Signature>> endorsement = new HashMap<>();
    private Map<ReceiptKey, Map<Integer, Signature>> receipts = new HashMap<>();
}
