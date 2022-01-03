import org.jetbrains.annotations.NotNull;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

public class Encryptor {

    public static final int AES_128 = 128;
    public static final int AES_192 = 192;
    public static final int AES_256 = 256;

    private static final String AES_CBC_NOPADDING = "AES/CBC/NoPadding";

    private static final String AES = "AES";

    public static SecretKey generateNewKey(int keySize) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(AES);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGenerator.init(keySize);
        return keyGenerator.generateKey();
    }

    public static IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static String[] encrypt(String plainText) {
        byte[] key = generateNewKey(AES_256).getEncoded();
        byte[] iv = generateIV().getIV();
        byte[][] ivAndCipher = encrypt(iv, key, plainText);
        return new String[]{byteArrayToHexString(iv), byteArrayToHexString(key), byteArrayToHexString(ivAndCipher[1])};
    }

    public static byte[][] encrypt(byte[] iv, byte[] key, String plainText) {
        return encrypt(iv, key, plainText.getBytes());
    }

    public static byte[][] encrypt(byte[] iv, byte[] key, byte[] plainText) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_NOPADDING);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] cipherText = cipher.doFinal(pad(plainText));
            return new byte[][]{iv, cipherText};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String iv, String key, String cipherText) {
        return new String(Objects.requireNonNull(decrypt(hexStringToByteArray(iv), hexStringToByteArray(key), hexStringToByteArray(cipherText))));
    }

    public static byte[] decrypt(byte[] iv, byte[] key, byte[] cipherText) {
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance(AES_CBC_NOPADDING);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher.doFinal(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteArrayToHexString(byte[] arg) {
        try {
            int len = arg.length * 2;
            return String.format("%0" + len + "x", new BigInteger(1, arg));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] hexStringToByteArray(@NotNull String input) {
        byte[] byteArray = new byte[input.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) Integer.parseInt(input.substring(2 * i, 2 * i + 2), 16);
        }
        return byteArray;
    }

    public static String pad(String plainText) {
        while (plainText.length() % 16 > 0) {
            plainText += " ";
        }
        return plainText;
    }

    public static byte[] pad(byte[] plainText) {
        ArrayList<Byte> byteList = new ArrayList<>();
        for (byte b : plainText) {
            byteList.add(b);
        }
        while (byteList.size() % 16 > 0) {
            byteList.add((byte) 32);
        }
        byte[] padded = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            padded[i] = byteList.get(i);
        }
        return padded;
    }

}
