import com.errorgon.Encryptor.Encryptor;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class EncryptorTest {


    @Test
    public void encryptAndDecrypt16Test() {
        String plainText = new String("ThisIs16CharLong");

        byte[] key = Encryptor.generateNewKey(Encryptor.AES_256).getEncoded();
        byte[] iv = Encryptor.generateIV().getIV();
        byte[][] ivAndCipher = Encryptor.encrypt(iv, key, plainText.getBytes());

        byte[] result = Encryptor.decrypt(ivAndCipher[0], key, ivAndCipher[1]);

        Assert.assertEquals("ThisIs16CharLong", new String(result));
    }

    @Test
    public void encryptAndDecrypt32Test() {
        String plainText = new String("LongerMessageIsRoughly32CharLong");

        byte[] key = Encryptor.generateNewKey(Encryptor.AES_256).getEncoded();
        byte[] iv = Encryptor.generateIV().getIV();
        byte[][] ivAndCipher = Encryptor.encrypt(iv, key, plainText.getBytes());

        byte[] result = Encryptor.decrypt(ivAndCipher[0], key, ivAndCipher[1]);

        Assert.assertEquals("LongerMessageIsRoughly32CharLong", new String(result));
    }

    @Test
    public void encryptAndDecryptPartialTest() {
        String plainText = new String("MessageNotMultipleOf16Chars");

        byte[] key = Encryptor.generateNewKey(Encryptor.AES_256).getEncoded();
        byte[] iv = Encryptor.generateIV().getIV();
        byte[][] ivAndCipher = Encryptor.encrypt(iv, key, plainText.getBytes());

        byte[] result = Encryptor.decrypt(ivAndCipher[0], key, ivAndCipher[1]);
        Assert.assertEquals("MessageNotMultipleOf16Chars", new String(result).trim());
    }

    @Test
    public void decrypt32Test() {
        String key = "d41468375496980fcee3cd5e36583f4acf30b4f070691fc81c77d9e53a45a4fa";
        String iv = "7668591bf76e5e5a84930ca579dad07a";
        String ciperText = "a16475bf17b24ac78180ae57e81595cfe93acb3b6466a4c16254aa50788a6c57";

        Assert.assertEquals("HelloWorldHelloWorldHelloWorldHe", Encryptor.decrypt(iv, key, ciperText));
    }

    @Test
    public void encryptStringOnlyTest() {
        String plainText = new String("LongerMessageIsRoughly32CharLong");
            String[] ivKeyCipher = Encryptor.encrypt(plainText);
        String result = Encryptor.decrypt(ivKeyCipher[0], ivKeyCipher[1], ivKeyCipher[2]);

        Assert.assertEquals(plainText, result);
    }


    @Test
    public void hexStringToByteArrayTest() {
        String input = "0B0A0901";
        byte[] result = Encryptor.hexStringToByteArray(input);
        Assert.assertEquals(Arrays.toString(new byte[]{11, 10, 9, 1}), Arrays.toString(result));
    }

    @Test
    public void byteArrayToHexStringTest() {
        byte input [] = new byte[]{11, 10, 9, 1};
        String result = Encryptor.byteArrayToHexString(input);
        Assert.assertEquals("0B0A0901", result.toUpperCase());
    }

    @Test
    public void paddingStringTest() {
        Assert.assertEquals("Hello World     ", Encryptor.pad("Hello World"));
        Assert.assertEquals("Hello World     ", Encryptor.pad("Hello World     "));
        Assert.assertEquals("Hello World                     ", Encryptor.pad("Hello World      "));
    }

    @Test
    public void paddingByteTest() {
        byte[] input = new byte[]{72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100};
        byte[] expected = new byte[]{72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 32, 32, 32, 32, 32};
        Assert.assertEquals(Arrays.toString(expected), Arrays.toString(Encryptor.pad(input)));
    }

}