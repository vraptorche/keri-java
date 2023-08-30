package foundation.identity.keri.internal.identifier;

import foundation.identity.keri.api.identifier.SelfSigningIdentifier;
import foundation.identity.keri.crypto.Signature;

public record ImmutableSelfSigningIdentifier(
        Signature signature
) implements SelfSigningIdentifier {


}
