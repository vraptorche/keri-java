package foundation.identity.keri.crypto;

public record SignatureRecord(
        SignatureAlgorithm algorithm,
        byte[] bytes
) implements Signature {


}