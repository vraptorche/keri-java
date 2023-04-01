package foundation.identity.keri.internal;

import foundation.identity.keri.api.Version;

public record ImmutableVersion(int major, int minor) implements Version {

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ImmutableVersion other)) {
      return false;
    }
    return (this.major == other.major) && (this.minor == other.minor);
  }

  @Override
  public String toString() {
    return this.major + "." + this.minor;
  }
}
