package foundation.identity.keri.crypto;

import java.util.Objects;

public interface EdDSAParameters extends SignatureAlgorithmParameters {

  static boolean equals(EdDSAParameters parameters, Object o) {
    if (parameters == o) {
      return true;
    }

    if (o == null) {
      return false;
    }

    if (!(o instanceof EdDSAParameters otherAlgorithm)) {
      return false;
    }

    return parameters.curveName().equals(otherAlgorithm.curveName());
  }

  static int hashCode(EdDSAParameters algorithm) {
    return Objects.hash(algorithm.curveName());
  }

  String curveName();

}
