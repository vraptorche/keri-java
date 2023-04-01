package foundation.identity.keri.internal.identifier;

import foundation.identity.keri.api.identifier.BasicIdentifier;

import java.security.PublicKey;

public record ImmutableBasicIdentifier(PublicKey publicKey) implements BasicIdentifier {

  @Override
  public int hashCode() {
    return BasicIdentifier.hashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BasicIdentifier)) {
      return false;
    }
    return BasicIdentifier.equals(this, (BasicIdentifier) obj);
  }

  @Override
  public String toString() {
    return "ImmutableBasicPrefix [publicKey=" + this.publicKey + "]";
  }

}
