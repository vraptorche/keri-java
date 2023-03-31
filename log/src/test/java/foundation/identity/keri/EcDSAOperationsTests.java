package foundation.identity.keri;

import foundation.identity.keri.crypto.EcDSAOperations;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;

import static org.junit.jupiter.api.Assertions.*;

 class EcDSAOperationsTests {

	ECParameterSpec parameterSpec;

	@BeforeAll
	 static void beforeClass() {
		// secp256k1 is considered "unsecure" so you have enable it like this:
		System.setProperty("jdk.sunec.disableNative", "false");
	}

	@BeforeEach
	 void setUp() throws GeneralSecurityException {
		var ap = AlgorithmParameters.getInstance("EC", new BouncyCastleProvider());
		ap.init(new ECGenParameterSpec("secp256k1"));
		this.parameterSpec = ap.getParameterSpec(ECParameterSpec.class);
	}

	@Test
	 void test_EC_SECP256K1_generateKeyPair() {
		var ops = EcDSAOperations.EC_SECP256K1;
		var result = ops.generateKeyPair();

		assertEquals("EC", result.getPrivate().getAlgorithm());
		assertEquals("EC", result.getPublic().getAlgorithm());
	}

	@Test
	 void test_EC_SECP256K1_encode() throws GeneralSecurityException {
		var w = new ECPoint(
				new BigInteger("c34404f02d7db7382b9ab4c9afd1f6899a8146b694f52b4642d7f083db53c8e0", 16),
				new BigInteger("e17c8a229704c4b0337e84b0fae73d3d4c0870b009ba77a7f000681d3862f88f", 16));
		var keyFactory = KeyFactory.getInstance("EC");
		var spec = new ECPublicKeySpec(w, this.parameterSpec);
		var publicKey = (ECPublicKey) keyFactory.generatePublic(spec);

		var ops = EcDSAOperations.EC_SECP256K1;
		var result = ops.encode(publicKey);

		var expected = Hex.unhex(
				"03c34404f02d7db7382b9ab4c9afd1f6899a8146b694f52b4642d7f083db53c8e0");
		assertArrayEquals(expected, result);
	}

	@Test
	 void test_EC_SECP256K1_decode() {
		var encoded = Hex.unhex(
				"03c34404f02d7db7382b9ab4c9afd1f6899a8146b694f52b4642d7f083db53c8e0");

		var ops = EcDSAOperations.EC_SECP256K1;
		var result = (ECPublicKey) ops.publicKey(encoded);

		assertEquals("EC", result.getAlgorithm());
		// TODO parameter spec equality
		assertEquals(this.parameterSpec.getCurve(), result.getParams().getCurve());

		var expectedPoint = new ECPoint(
				new BigInteger("c34404f02d7db7382b9ab4c9afd1f6899a8146b694f52b4642d7f083db53c8e0", 16),
				new BigInteger("e17c8a229704c4b0337e84b0fae73d3d4c0870b009ba77a7f000681d3862f88f", 16));
		assertEquals(expectedPoint, result.getW());
	}

	@Test
	 void test_EC_SECP256K1_encodeDecodeRoundtrip() throws GeneralSecurityException {
		var w = new ECPoint(
				new BigInteger("c34404f02d7db7382b9ab4c9afd1f6899a8146b694f52b4642d7f083db53c8e0", 16),
				new BigInteger("e17c8a229704c4b0337e84b0fae73d3d4c0870b009ba77a7f000681d3862f88f", 16));
		var spec = new ECPublicKeySpec(w, this.parameterSpec);
		var keyFactory = KeyFactory.getInstance("EC");
		var publicKey = (ECPublicKey) keyFactory.generatePublic(spec);

		var ops = EcDSAOperations.EC_SECP256K1;

		var bytes = ops.encode(publicKey);
		var decoding = (ECPublicKey) ops.publicKey(bytes);

		assertEquals("EC", decoding.getAlgorithm());
		// TODO parameter spec equality
		final ECParameterSpec paramsSpec = decoding.getParams();
		assertEquals(this.parameterSpec.getCurve(), paramsSpec.getCurve());

		assertEquals(w, decoding.getW());
	}

	@Test
	 void test_EC_SECP256K1_decodeEncodeRoundtrip() {
		var encoded = Hex.unhex(
				"03c34404f02d7db7382b9ab4c9afd1f6899a8146b694f52b4642d7f083db53c8e0");

		var ops = EcDSAOperations.EC_SECP256K1;
		var publicKey = ops.publicKey(encoded);
		var bytes = ops.encode(publicKey);

		assertArrayEquals(encoded, bytes);
	}

	@Test
	 void test_EC_SECP256K1_signVerify() throws GeneralSecurityException {
		var skb = new BigInteger("00eb33adf5364133e53e43291bccb799cf24024ecb09547a4210b44e4e28936187", 16);
		var pkb = Hex.unhex(
				"04c34404f02d7db7382b9ab4c9afd1f6899a8146b694f52b4642d7f083db53c8e0e17c8a229704c4b0337e84b0fae73d3d4c0870b009ba77a7f000681d3862f88f");
		var msg = Hex.unhex("72");

		var privateKeySpec = new ECPrivateKeySpec(skb, this.parameterSpec);
		var kf = KeyFactory.getInstance("EC", new BouncyCastleProvider());
		var privateKey = (ECPrivateKey) kf.generatePrivate(privateKeySpec);

		var ops = EcDSAOperations.EC_SECP256K1;
		var sig = ops.sign(msg, privateKey);

		var publicKey = ops.publicKey(pkb);
		assertTrue(ops.verify(msg, sig, publicKey));
	}

}
