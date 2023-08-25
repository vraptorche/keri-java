package foundation.identity.keri.internal.event;

import foundation.identity.keri.api.event.SigningThreshold.Weighted.Weight;

import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ImmutableWeight implements Weight {

    private final int numerator;
    private final Integer denominator;

    public ImmutableWeight(int numerator, Integer denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static ImmutableWeight of(Weight weight) {
        if (weight instanceof ImmutableWeight immutableWeight) {
            return immutableWeight;
        }
        requireNonNull(weight, "weight");
        return new ImmutableWeight(weight.numerator(), weight.denominator().orElse(1));
    }

    @Override
    public int numerator() {
        return this.numerator;
    }

    @Override
    public Optional<Integer> denominator() {
        return Optional.ofNullable(this.denominator);
    }

    @Override
    public ImmutableWeight normalize() {
        return new ImmutableWeight(this.numerator, this.denominator().orElse(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Weight that)) {
            return false;
        }
        return this.numerator == that.numerator()
                && Objects.equals(this.denominator, that.denominator().orElse(1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.numerator, this.denominator);
    }

    @Override
    public String toString() {
        return this.denominator()
                .filter(d -> !d.equals(1))
                .map(d -> "%d/%d".formatted(this.numerator, d))
                .orElse(String.valueOf(this.numerator));
    }
}
