# Encryptor
Java library to help make AES/CBC/NoPadding encryption easier.

## Installation
Library can be installed as a gradle or maven project

### Gradle/Android

Add the jitpack repository to build.gradle (project) file
```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Add the Encryptor library as a dependency to the build.gradle (module) file
```
dependencies {
    implementation 'com.github.errorgon:encryptor:1.0.0'
}
```

### Maven

Add the jitpack repository and library dependency to your pom.xml file
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.errorgon</groupId>
        <artifactId>Encryptor</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Usage
The easiest way to use the library is to call the static encrypt function on a string of plain text. 
The method returns a 3-element array of strings that includes the initialization vector (IV), encryption key (AES-256 by default), and cipher text/encrypted message.

### Encrypting
```
// CBC method of encrypting requires a plainText length in multiples of 16 bytes
// The encryption methods in the library will pad the plainText with spaces (i.e. byte b = 32, 0x20, U+0020)
String plainText = "Chars to Encrypt";

// static method Encryptor.encrypt(plainText) returns a String[] with elements {iv, key, cipherText}
String[] ivKeyCipher = Encryptor.encrypt(plainText);

// The resulting array, represented as strings of hex values, looks like this:
String iv = ivKeyCipher[0];
String key = ivKeyCipher[1];
String cipherText = ivKeyCipher[2];

System.out.println(iv);         // 5262e5d3d4761a851bbe6f98dd58a279
System.out.println(key);        // e31465e12f54711b115597a13d46826086efb83747825b805b36fa69d2ba5584
System.out.println(cipherText); // 9e3031a8c1dd735b2dc9df47b87ba0b9
```

### Decrypting
The output of the encryption method can be used in the decrypt method
```
String decryptedMessage = Encryptor.decrypt(iv, key, cipherText);

// decryptedMessage will equal the original plainText string if the plainText 
// length was a multiple of 16. Otherwise, call the .trim() method on the 
// decryptedMessage to remove the padding added during encryption process
```

#### Overloaded Methods
The encrypt and decrypt methods can also accept byte arrays if the user needs to specify their own keys and IVs. 
```
String plainText = new String("MessageNotMultipleOf16Chars");

byte[] key = Encryptor.generateNewKey(Encryptor.AES_256).getEncoded();
byte[] iv = Encryptor.generateIV().getIV();
byte[][] ivAndCipher = Encryptor.encrypt(iv, key, plainText.getBytes());

byte[] result = Encryptor.decrypt(ivAndCipher[0], key, ivAndCipher[1]);

System.out.println(new String(result).trim()); // Output: MessageNotMultipleOf16Chars
```





