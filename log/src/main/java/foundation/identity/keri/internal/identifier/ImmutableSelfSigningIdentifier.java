package foundation.identity.keri.internal.identifier;

import foundation.identity.keri.QualifiedBase64;
import foundation.identity.keri.api.identifier.SelfSigningIdentifier;
import foundation.identity.keri.crypto.Signature;

import java.util.Objects;

public record ImmutableSelfSigningIdentifier(Signature signature) implements SelfSigningIdentifier {

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    var other = (ImmutableSelfSigningIdentifier) obj;
    return Objects.equals(this.signature, other.signature);
  }

  @Override
  public String toString() {
    return QualifiedBase64.qb64(this);
  }

}
