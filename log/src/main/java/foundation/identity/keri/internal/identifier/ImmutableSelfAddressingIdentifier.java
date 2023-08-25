package foundation.identity.keri.internal.identifier;

import foundation.identity.keri.QualifiedBase64;
import foundation.identity.keri.api.identifier.SelfAddressingIdentifier;
import foundation.identity.keri.crypto.Digest;

import java.util.Objects;

public record ImmutableSelfAddressingIdentifier(
        Digest digest
) implements SelfAddressingIdentifier {
    @Override
    public String toString() {
        return QualifiedBase64.qb64(this);
    }

}
