package foundation.identity.keri.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

import static foundation.identity.keri.SigningThresholds.*;
import static foundation.identity.keri.controller.KeyEventSerializer.signingThreshold;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceUnderscores.class)
class KeyEventSerializerTests {

  final ObjectMapper mapper = new ObjectMapper();

  @Test
  void signingThreshold__unweighted() {
    assertEquals("\"1\"", signingThreshold(unweighted(1), this.mapper).toString());

    assertEquals(
        "\"2\"",
        signingThreshold(unweighted(2), this.mapper).toString());

    assertEquals(
        "\"3\"",
        signingThreshold(unweighted(3), this.mapper).toString());
  }

  @Test
  void signingThreshold__weighted() {

    // ["1/2", "1/2", "1/4", "1/4", "1/4"]
    assertEquals(
        "[[\"1/2\",\"1/2\",\"1/4\",\"1/4\",\"1/4\"]]",
        signingThreshold(
            weighted("1/2", "1/2", "1/4", "1/4", "1/4"),
            this.mapper)
            .toString());

    // [["1/2", "1/2", "1/4", "1/4", "1/4"]]
    assertEquals(
        "[[\"1/2\",\"1/2\",\"1/4\",\"1/4\",\"1/4\"]]",
        signingThreshold(
            weighted(
                group("1/2", "1/2", "1/4", "1/4", "1/4")),
            this.mapper)
            .toString());

    // [["1/2","1/2","1/4","1/4","1/4"],["1","1"]]
    assertEquals(
        "[[\"1/2\",\"1/2\",\"1/4\",\"1/4\",\"1/4\"],[\"1\",\"1\"]]",
        signingThreshold(
            weighted(
                group("1/2", "1/2", "1/4", "1/4", "1/4"),
                group("1", "1")),
            this.mapper)
            .toString());
  }

}
