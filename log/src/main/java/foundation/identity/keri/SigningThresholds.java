package foundation.identity.keri;

import foundation.identity.keri.api.event.SigningThreshold;
import foundation.identity.keri.api.event.SigningThreshold.Weighted.Weight;
import foundation.identity.keri.internal.event.ImmutableUnweightedSigningThreshold;
import foundation.identity.keri.internal.event.ImmutableWeight;
import foundation.identity.keri.internal.event.ImmutableWeightedSigningThreshold;
import org.apache.commons.math3.fraction.Fraction;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class SigningThresholds {

	public static SigningThreshold.Unweighted unweighted(int threshold) {
		if (threshold <= 0) {
			throw new IllegalArgumentException("threshold must be greater than 0");
		}

		return new ImmutableUnweightedSigningThreshold(threshold);
	}

	public static SigningThreshold.Weighted weighted(Weight... weights) {
		return weighted(new Weight[][]{weights});
	}

	public static SigningThreshold.Weighted weighted(String... weightsAsStrings) {
		var weights = Stream.of(weightsAsStrings)
				.map(SigningThresholds::weight)
				.toArray(Weight[]::new);

		return weighted(weights);
	}

	public static SigningThreshold.Weighted weighted(Weight[]... weightGroups) {
		for (var group : weightGroups) {
			if (!sumGreaterThanOrEqualToOne(group)) {
				throw new IllegalArgumentException("group sum is less than 1: " + Arrays.deepToString(group));
			}
		}

		return new ImmutableWeightedSigningThreshold(weightGroups);
	}

	private static boolean sumGreaterThanOrEqualToOne(Weight[] weights) {
		var sum = Fraction.ZERO;
		for (var w : weights) {
			//noinspection ObjectAllocationInLoop
			sum = sum.add(fraction(w));
		}

		return sum.compareTo(Fraction.ONE) >= 0;
	}

	public static Weight weight(String value) {
		var parts = value.split("/");
		if (parts.length == 1) {
			return weight(Integer.parseInt(parts[0]));
		} else if (parts.length == 2) {
			return weight(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
		} else {
			throw new IllegalArgumentException("invalid weight: " + value);
		}
	}

	public static Weight weight(int value) {
		return weight(value, null);
	}

	public static Weight weight(int numerator, Integer denominator) {
		if (denominator != null && denominator <= 0) {
			throw new IllegalArgumentException("denominator must be > 0");
		}

		if (numerator <= 0) {
			throw new IllegalArgumentException("numerator must be > 0");
		}

		return new ImmutableWeight(numerator, denominator);
	}

	public static Weight[] group(Weight... weights) {
		return weights;
	}

	public static Weight[] group(String... weights) {
		return Stream.of(weights)
				.map(SigningThresholds::weight)
				.toArray(Weight[]::new);
	}

	public static boolean thresholdMet(SigningThreshold threshold, int[] indexes) {
		if (threshold instanceof SigningThreshold.Unweighted unweighted) {
			return thresholdMet(unweighted, indexes);
		} else if (threshold instanceof SigningThreshold.Weighted weighted) {
			return thresholdMet(weighted, indexes);
		} else {
			throw new IllegalArgumentException("Unknown threshold type: " + threshold.getClass().getCanonicalName());
		}
	}

	public static boolean thresholdMet(SigningThreshold.Unweighted threshold, int[] indexes) {
		requireNonNull(indexes, "indexes");
		return indexes.length >= threshold.threshold();
	}

	public static boolean thresholdMet(SigningThreshold.Weighted threshold, int[] indexes) {
		requireNonNull(indexes);

		if (indexes.length == 0) {
			return false;
		}

		var maxIndex = IntStream.of(indexes)
				.max()
				.getAsInt();
		var countWeights = countWeights(threshold.weights());

		var sats = prefillSats(Integer.max(maxIndex + 1, countWeights));
		for (var i : indexes) {
			sats[i] = true;
		}

		var index = 0;
		for (var clause : threshold.weights()) {
			var accumulator = Fraction.ZERO;
			for (var weight : clause) {
				if (sats[index]) {
					accumulator = accumulator.add(fraction(weight));
				}
				index++;
			}

			if (accumulator.compareTo(Fraction.ONE) < 0) {
				return false;
			}
		}

		return true;
	}

	public static int countWeights(Weight[][] weights) {
		return Arrays.stream(weights)
				.mapToInt(w -> w.length)
				.sum();
	}

	private static boolean[] prefillSats(int count) {
		var sats = new boolean[count];
		Arrays.fill(sats, false);
		return sats;
	}

	private static Fraction fraction(Weight weight) {
		if (weight.denominator().isEmpty()) {
			return new Fraction(weight.numerator());
		}

		return new Fraction(weight.numerator(), weight.denominator().get());
	}
}
