package foundation.identity.keri.internal.seal;

import foundation.identity.keri.api.seal.DigestSeal;
import foundation.identity.keri.crypto.Digest;

public record ImmutableDigestSeal(Digest digest) implements DigestSeal {

}
