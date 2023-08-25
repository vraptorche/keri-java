package foundation.identity.keri;

import foundation.identity.keri.api.event.SigningThreshold;
import foundation.identity.keri.api.event.SigningThreshold.Weighted.Weight;
import foundation.identity.keri.internal.event.ImmutableWeight;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static foundation.identity.keri.SigningThresholds.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(ReplaceUnderscores.class)
class SigningThresholdsTests {

  @Test
  void test__weight__int_int() {
    assertWeightValue(weight(1, 1), 1, 1);
    assertWeightValue(weight(1, 2), 1, 2);

    assertWeightValue(weight(2, 1), 2, 1);
    assertWeightValue(weight(2, 2), 2, 2);

    assertWeightValue(weight(2, 3), 2, 3);
    assertWeightValue(weight(3, 2), 3, 2);

    assertThrows(IllegalArgumentException.class, () -> weight(0, 1));
    assertThrows(IllegalArgumentException.class, () -> weight(1, 0));
    assertThrows(IllegalArgumentException.class, () -> weight(-1, 2));
    assertThrows(IllegalArgumentException.class, () -> weight(1, -2));
    assertThrows(IllegalArgumentException.class, () -> weight(-1, -2));
  }

  @Test
  void test__weight__int() {
    assertThrows(IllegalArgumentException.class, () -> weight(-1));
    assertThrows(IllegalArgumentException.class, () -> weight(0));

    assertEquals(new ImmutableWeight(1, 1), weight(1));
    assertEquals(new ImmutableWeight(2, 1), weight(2));
    assertEquals(new ImmutableWeight(1, 1), weight(1, 1));
    assertEquals(new ImmutableWeight(1, 2), weight(1, 2));
    assertEquals(new ImmutableWeight(1, 3), weight(1, 3));
  }

  @Test
  void test__weight__String() {
    assertEquals(weight("1/1"), weight(1, 1));
    assertEquals(weight("1/2"), weight(1, 2));

    assertEquals(weight("2/1"), weight(2, 1));
    assertEquals(weight("2/2"), weight(2, 2));

    assertEquals(weight("2/3"), weight(2, 3));
    assertEquals(weight("3/2"), weight(3, 2));


    assertThrows(IllegalArgumentException.class, () -> weight("0/1"));
    assertThrows(IllegalArgumentException.class, () -> weight("1/0"));
    assertThrows(IllegalArgumentException.class, () -> weight("-1/2"));
    assertThrows(IllegalArgumentException.class, () -> weight("1/-2"));
    assertThrows(IllegalArgumentException.class, () -> weight("-1/-2"));
  }

  private void assertWeightValue(Weight w, int expectedNumerator, int expectedDenominator) {
    assertEquals(expectedNumerator, w.numerator(), "numerator");
    assertEquals((Integer) expectedDenominator, w.denominator().orElse(null), "denominator");
  }

  @Test
  void test__unweighted() {
    assertThrows(IllegalArgumentException.class, () -> unweighted(-1));
    assertThrows(IllegalArgumentException.class, () -> unweighted(0));
  }

  @Test
  void test__weighted() {
    SigningThreshold.Weighted weightedGroup = weighted(group("1"));
    SigningThreshold.Weighted weighted = weighted("1");
    assertEquals(weightedGroup, weighted);

    assertEquals(
        weighted(
            group(
                weight(1, 2),
                weight(1, 2),
                weight(1, 4),
                weight(1, 4),
                weight(1, 4)
            )),
        weighted("1/2", "1/2", "1/4", "1/4", "1/4"));

    assertEquals(
        weighted(
            group(
                weight(1, 2),
                weight(1, 2),
                weight(1, 4),
                weight(1, 4),
                weight(1, 4)
            ),
            group(
                weight(1),
                weight(1)
            )),
        weighted(
            group("1/2", "1/2", "1/4", "1/4", "1/4"),
            group("1", "1")));
  }

  @Test
  void test__weighted__WeightArray() {
    assertThrows(IllegalArgumentException.class, (() -> weighted(weight("1/3"), weight("1/2"))));
    assertThrows(IllegalArgumentException.class, (() -> weighted(group("1/3", "1/2"), group("1"))));
    assertThrows(IllegalArgumentException.class, () -> weighted("0/1"));
    assertThrows(IllegalArgumentException.class, () -> weighted("1/0"));
  }

  @Test
  void test__weighted__StringArray() {
    assertThrows(IllegalArgumentException.class, () -> weighted("1/3", "1/2"));
    assertThrows(IllegalArgumentException.class, (() -> weighted(group("1/3", "1/2"), group("1"))));
    assertThrows(IllegalArgumentException.class, () -> weighted("0/1"));
    assertThrows(IllegalArgumentException.class, () -> weighted("1/0"));
  }

  @Test
  void test__thresholdMet__Unweighted() {
    assertFalse(thresholdMet(unweighted(2), ints(0)));
    assertTrue(thresholdMet(unweighted(2), ints(0, 1)));
    assertTrue(thresholdMet(unweighted(2), ints(0, 1, 2)));
  }

  @Nested
  class WeightedTest {
    @Test
    void threshold_met() {
      var threshold = weighted("1", "1");
      assertFalse(thresholdMet(threshold, new int[]{}));
      assertFalse(thresholdMet(threshold, ints()));
      assertTrue(thresholdMet(threshold, ints(1)));
      assertTrue(thresholdMet(threshold, ints(0, 1)));
    }

    @Test
    void threshold_met_weight_ratios() {
      var threshold = weighted("1/2", "1/2", "1/4", "1/4", "1/4");
      assertTrue(thresholdMet(threshold, ints(0, 2, 4)));
      assertTrue(thresholdMet(threshold, ints(0, 1)));
      assertTrue(thresholdMet(threshold, ints(1, 3, 4)));
      assertTrue(thresholdMet(threshold, ints(0, 1, 2, 3, 4)));
      assertTrue(thresholdMet(threshold, ints(3, 2, 0)));
      assertTrue(thresholdMet(threshold, ints(0, 0, 1, 2, 1)));
      assertFalse(thresholdMet(threshold, ints(0, 2)));
      assertFalse(thresholdMet(threshold, ints(2, 3, 4)));
    }

    @Test
    void threshold_met_weight_group() {
      var threshold = weighted(group("1/2", "1/2", "1/4", "1/4", "1/4"));
      assertTrue(thresholdMet(threshold, ints(0, 2, 4)));
      assertTrue(thresholdMet(threshold, ints(0, 1)));
      assertTrue(thresholdMet(threshold, ints(1, 3, 4)));
      assertTrue(thresholdMet(threshold, ints(0, 1, 2, 3, 4)));
      assertTrue(thresholdMet(threshold, ints(3, 2, 0)));
      assertTrue(thresholdMet(threshold, ints(0, 0, 1, 2, 1)));
      assertFalse(thresholdMet(threshold, ints(0, 2)));
      assertFalse(thresholdMet(threshold, ints(2, 3, 4)));
    }

    @Test
    void threshold_met_weight_multiple_groups() {
      var threshold = weighted(
          group("1/2", "1/2", "1/4", "1/4", "1/4"),
          group("1", "1"));
      assertTrue(thresholdMet(threshold, ints(1, 2, 3, 5)));
      assertTrue(thresholdMet(threshold, ints(0, 1, 6)));
      assertFalse(thresholdMet(threshold, ints(0, 1)));
      assertFalse(thresholdMet(threshold, ints(5, 6)));
      assertFalse(thresholdMet(threshold, ints(2, 3, 4)));
      assertFalse(thresholdMet(threshold, ints()));
    }
  }

  @Test
  void test__thresholdMet__Weighted() {
    assertTrue(thresholdMet(weighted(weight(1)), ints(0)));


  }

  private static int[] ints(int... ints) {
    return ints;
  }

}
