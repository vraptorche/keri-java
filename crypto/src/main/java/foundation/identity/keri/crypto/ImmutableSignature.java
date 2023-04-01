package foundation.identity.keri.crypto;

public record ImmutableSignature(SignatureAlgorithm algorithm, byte[] bytes) implements Signature {



}