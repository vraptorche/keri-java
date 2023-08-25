package foundation.identity.keri.internal;

import foundation.identity.keri.api.Version;

public record VersionRecord(
        int major,
        int minor) implements Version {

    @Override
    public String toString() {
        return this.major + "." + this.minor;
    }
}
