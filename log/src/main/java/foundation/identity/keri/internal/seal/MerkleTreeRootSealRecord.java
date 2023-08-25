package foundation.identity.keri.internal.seal;

import foundation.identity.keri.api.seal.MerkleTreeRootSeal;
import foundation.identity.keri.crypto.Digest;

public record MerkleTreeRootSealRecord(
        Digest digest
) implements MerkleTreeRootSeal {

}
