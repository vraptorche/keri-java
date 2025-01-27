package foundation.identity.keri.controller.spec;

import foundation.identity.keri.KeyConfigurationDigester;
import foundation.identity.keri.SigningThresholds;
import foundation.identity.keri.api.KeyState;
import foundation.identity.keri.api.event.Format;
import foundation.identity.keri.api.event.KeyConfigurationDigest;
import foundation.identity.keri.api.event.KeyEventCoordinates;
import foundation.identity.keri.api.event.SigningThreshold;
import foundation.identity.keri.api.event.StandardFormats;
import foundation.identity.keri.api.identifier.BasicIdentifier;
import foundation.identity.keri.api.identifier.Identifier;
import foundation.identity.keri.api.seal.Seal;
import foundation.identity.keri.crypto.Digest;
import foundation.identity.keri.crypto.DigestAlgorithm;
import foundation.identity.keri.crypto.StandardDigestAlgorithms;
import foundation.identity.keri.internal.event.ImmutableKeyEventCoordinates;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class RotationSpec {

	private final Format format;

	private final Identifier identifier;
	private final long sequenceNumber;
	private final KeyEventCoordinates previous;

	private final SigningThreshold signingThreshold;
	private final List<PublicKey> keys;
	private final Signer signer;

	private final KeyConfigurationDigest nextKeys;

	private final int witnessThreshold;
	private final List<BasicIdentifier> addedWitnesses;
	private final List<BasicIdentifier> removedWitnesses;

	private final List<Seal> seals;

	public RotationSpec(
			Format format,
			Identifier identifier,
			long sequenceNumber,
			KeyEventCoordinates previousEvent,
			SigningThreshold signingThreshold,
			List<PublicKey> keys,
			Signer signer,
			KeyConfigurationDigest nextKeys,
			int witnessThreshold,
			List<BasicIdentifier> removedWitnesses,
			List<BasicIdentifier> addedWitnesses,
			List<Seal> seals) {
		this.format = format;
		this.identifier = identifier;
		this.sequenceNumber = sequenceNumber;
		this.previous = previousEvent;
		this.signingThreshold = signingThreshold;
		this.keys = List.copyOf(keys);
		this.signer = signer;
		this.nextKeys = nextKeys;
		this.witnessThreshold = witnessThreshold;
		this.addedWitnesses = List.copyOf(addedWitnesses);
		this.removedWitnesses = List.copyOf(removedWitnesses);
		this.seals = List.copyOf(seals);
	}

	public static Builder builder(KeyState state) {
		return new Builder(state);
	}

	public Identifier identifier() {
		return this.identifier;
	}

	public long sequenceNumber() {
		return this.sequenceNumber;
	}

	public KeyEventCoordinates previous() {
		return this.previous;
	}

	public Format format() {
		return this.format;
	}

	public SigningThreshold signingThreshold() {
		return this.signingThreshold;
	}

	public List<PublicKey> keys() {
		return this.keys;
	}

	public Signer signer() {
		return this.signer;
	}

	public KeyConfigurationDigest nextKeys() {
		return this.nextKeys;
	}

	public int witnessThreshold() {
		return this.witnessThreshold;
	}

	public List<BasicIdentifier> addedWitnesses() {
		return this.addedWitnesses;
	}

	public List<BasicIdentifier> removedWitnesses() {
		return this.removedWitnesses;
	}

	public List<Seal> seals() {
		return this.seals;
	}

	public static class Builder {
		private final KeyState state;

		private Format format = StandardFormats.JSON;

		// key configuration
		private SigningThreshold signingThreshold;
		private final List<PublicKey> keys = new ArrayList<>();
		private Signer signer;


		// next key configuration
		private SigningThreshold nextSigningThreshold;

		// provide nextKeys + digest algo, nextKeyDigests + digest algo, or nextKeysDigest
		private final DigestAlgorithm nextKeysAlgorithm = StandardDigestAlgorithms.BLAKE3_256;
		private final List<PublicKey> listOfNextKeys = new ArrayList<>();
		private final List<Digest> listOfNextKeyDigests = new ArrayList<>();
		private KeyConfigurationDigest nextKeyConfigurationDigest = KeyConfigurationDigest.NONE;

		private final List<Seal> seals = new ArrayList<>();

		private int witnessThreshold = 0;
		private final List<BasicIdentifier> witnesses = new ArrayList<>();

		public Builder(KeyState state) {
			this.state = state;
		}

		public Builder json() {
			this.format = StandardFormats.JSON;
			return this;
		}

		public Builder cbor() {
			this.format = StandardFormats.CBOR;
			return this;
		}

		public Builder messagePack() {
			this.format = StandardFormats.MESSAGE_PACK;
			return this;
		}

		public Builder signingThreshold(int signingThreshold) {
			if (signingThreshold < 1) {
				throw new IllegalArgumentException("signingThreshold must be 1 or greater");
			}

			this.signingThreshold = SigningThresholds.unweighted(signingThreshold);
			return this;
		}

		public Builder signingThreshold(SigningThreshold signingThreshold) {
			this.signingThreshold = signingThreshold;
			return this;
		}

		public Builder key(PublicKey publicKey) {
			this.keys.add(publicKey);
			return this;
		}

		public Builder keys(List<PublicKey> publicKeys) {
			this.keys.addAll(requireNonNull(publicKeys));
			return this;
		}

		public Builder signer(Signer signer) {
			this.signer = requireNonNull(signer);
			return this;
		}

		public Builder signer(int keyIndex, PrivateKey privateKey) {
			if (keyIndex < 0) {
				throw new IllegalArgumentException("keyIndex must be >= 0");
			}

			this.signer = new PrivateKeySigner(keyIndex, requireNonNull(privateKey));
			return this;
		}

		public Builder nextSigningThreshold(int nextSigningThreshold) {
			if (nextSigningThreshold < 1) {
				throw new IllegalArgumentException("nextSigningThreshold must be 1 or greater");
			}

			this.nextSigningThreshold = SigningThresholds.unweighted(nextSigningThreshold);
			return this;
		}

		public Builder nextSigningThreshold(SigningThreshold nextSigningThreshold) {
			this.nextSigningThreshold = requireNonNull(nextSigningThreshold);
			return this;
		}

		public Builder nextKeys(KeyConfigurationDigest nextKeysDigest) {
			this.nextKeyConfigurationDigest = requireNonNull(nextKeysDigest);
			return this;
		}

		public Builder witnessThreshold(int witnessThreshold) {
			if (witnessThreshold < 0) {
				throw new IllegalArgumentException("witnessThreshold must not be negative");
			}

			this.witnessThreshold = witnessThreshold;
			return this;
		}

		public Builder addWitness(BasicIdentifier prefix) {
			this.witnesses.add(requireNonNull(prefix));
			return this;
		}

		public Builder addWitnesses(List<BasicIdentifier> prefixes) {
			this.witnesses.addAll(requireNonNull(prefixes));
			return this;
		}

		public Builder addWitnesses(BasicIdentifier... prefixes) {
			Collections.addAll(this.witnesses, prefixes);
			return this;
		}

		public Builder removeWitness(BasicIdentifier identifier) {
			if (!this.witnesses.remove(requireNonNull(identifier))) {
				throw new IllegalArgumentException("witness not found in witness set");
			}
			return this;
		}

		public Builder removeWitnesses(List<BasicIdentifier> witnesses) {
			for (var witness : witnesses) {
				this.removeWitness(witness);
			}
			return this;
		}

		public Builder removeWitnesses(BasicIdentifier... witnesses) {
			for (var witness : witnesses) {
				this.removeWitness(witness);
			}
			return this;
		}

		public Builder seal(Seal seal) {
			this.seals.add(requireNonNull(seal));
			return this;
		}

		public Builder seals(List<Seal> seals) {
			this.seals.addAll(requireNonNull(seals));
			return this;
		}

		public RotationSpec build() {

			// --- KEYS ---

			if (this.keys.isEmpty()) {
				throw new IllegalArgumentException("No keys provided.");
			}

			if (this.signingThreshold == null) {
				this.signingThreshold = SigningThresholds.unweighted((this.keys.size() / 2) + 1);
			}

			if (this.signingThreshold instanceof SigningThreshold.Unweighted unw) {
				if (unw.threshold() > this.keys.size()) {
					throw new IllegalArgumentException(
							"Invalid unweighted signing threshold:"
									+ " keys: " + this.keys.size()
									+ " threshold: " + unw.threshold());
				}
			} else if (this.signingThreshold instanceof SigningThreshold.Weighted w) {
				var countOfWeights = Stream.of(w.weights())
						.mapToLong(wts -> wts.length)
						.sum();
				if (countOfWeights != this.keys.size()) {
					throw new IllegalArgumentException(
							"Count of weights and count of keys are not equal: "
									+ " keys: " + this.keys.size()
									+ " weights: " + countOfWeights);
				}
			} else {
				throw new IllegalArgumentException("Unknown SigningThreshold type: " + this.signingThreshold.getClass());
			}

			// --- NEXT KEYS ---

			if ((!this.listOfNextKeys.isEmpty() && (this.nextKeyConfigurationDigest != null))
					|| (!this.listOfNextKeys.isEmpty() && !this.listOfNextKeyDigests.isEmpty())
					|| (!this.listOfNextKeyDigests.isEmpty() && (this.nextKeyConfigurationDigest != null))) {
				throw new IllegalArgumentException("Only provide one of nextKeys, nextKeyDigests, or a nextKeys.");
			}

			if (this.nextKeyConfigurationDigest == null) {
				// if we don't have it, we use default of majority nextSigningThreshold
				if (this.nextSigningThreshold == null) {
					this.nextSigningThreshold = SigningThresholds.unweighted((this.keys.size() / 2) + 1);
				} else if (this.nextSigningThreshold instanceof SigningThreshold.Unweighted unw) {
					if (unw.threshold() > this.keys.size()) {
						throw new IllegalArgumentException(
								"Invalid unweighted signing threshold:"
										+ " keys: " + this.keys.size()
										+ " threshold: " + unw.threshold());
					}
				} else if (this.nextSigningThreshold instanceof SigningThreshold.Weighted w) {
					var countOfWeights = Stream.of(w.weights())
							.mapToLong(wts -> wts.length)
							.sum();
					if (countOfWeights != this.keys.size()) {
						throw new IllegalArgumentException(
								"Count of weights and count of keys are not equal: "
										+ " keys: " + this.keys.size()
										+ " weights: " + countOfWeights);
					}
				} else {
					throw new IllegalArgumentException("Unknown SigningThreshold type: " + this.nextSigningThreshold.getClass());
				}

				if (this.listOfNextKeyDigests.isEmpty()) {
					if (this.listOfNextKeys.isEmpty()) {
						throw new IllegalArgumentException("None of nextKeys, digestOfNextKeys, or nextKeyConfigurationDigest provided");
					}

					this.nextKeyConfigurationDigest = KeyConfigurationDigester.digest(this.nextSigningThreshold, this.listOfNextKeys, this.nextKeysAlgorithm);
				} else {
					this.nextKeyConfigurationDigest = KeyConfigurationDigester.digest(this.nextSigningThreshold, this.listOfNextKeyDigests);
				}
			}

			// --- WITNESSES ---
			var added = new ArrayList<>(this.witnesses);
			added.removeAll(this.state.witnesses());

			var removed = new ArrayList<>(this.state.witnesses());
			removed.removeAll(this.witnesses);

			return new RotationSpec(
					this.format,
					this.state.identifier(),
					this.state.lastEvent().sequenceNumber() + 1,
					ImmutableKeyEventCoordinates.of(this.state.lastEvent()),
					this.signingThreshold,
					this.keys,
					this.signer,
					this.nextKeyConfigurationDigest,
					this.witnessThreshold,
					removed,
					added,
					this.seals);
		}

	}

}
