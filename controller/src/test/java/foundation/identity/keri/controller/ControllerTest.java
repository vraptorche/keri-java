package foundation.identity.keri.controller;

import foundation.identity.keri.eventstorage.inmemory.InMemoryKeyEventStore;
import foundation.identity.keri.keystorage.inmemory.InMemoryIdentifierKeyStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

  private final InMemoryKeyEventStore keyEventStore = new InMemoryKeyEventStore();
  private final InMemoryIdentifierKeyStore keyStore = new InMemoryIdentifierKeyStore();
  private SecureRandom secureRandom = new SecureRandom(new byte[]{0});

  @BeforeEach
  public void beforeEachTest() throws NoSuchAlgorithmException {
    // this makes the values of secureRandom deterministic
    this.secureRandom = SecureRandom.getInstance("SHA1PRNG");
    this.secureRandom.setSeed(new byte[]{0});
  }

  @Test
  void newPublicIdentifier() {
    final Controller controller = new Controller(keyEventStore, keyStore, secureRandom);
    final ControllableIdentifier identifier = controller.newPublicIdentifier();
    identifier.delegatingIdentifier();
  }
}