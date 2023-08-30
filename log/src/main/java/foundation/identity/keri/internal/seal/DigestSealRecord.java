package foundation.identity.keri.internal.seal;

import foundation.identity.keri.api.seal.DigestSeal;
import foundation.identity.keri.crypto.Digest;

public record DigestSealRecord(Digest digest) implements DigestSeal {

}
