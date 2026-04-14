package is.hi.messagee2efront.service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.KeyFactory;
import java.util.Base64;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class KeyStorageService {
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 12;
    private static final int ITERATIONS = 65536;
    private static final int AES_KEY_LENGTH = 128;
    private static final int GCM_TAG_LENGTH = 128;

    public void savePrivateKey(String username, String password, PrivateKey privateKey){
        try {
            byte[] salt = randomBytes(SALT_LENGTH);
            byte[] iv = randomBytes(IV_LENGTH);

            SecretKey aesKey = deriveKey(password, salt);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] encryptedPrivateKey = cipher.doFinal(privateKey.getEncoded());

            ByteBuffer buffer = ByteBuffer.allocate(4 + salt.length + 4 + iv.length + encryptedPrivateKey.length);
            buffer.putInt(salt.length);
            buffer.put(salt);
            buffer.putInt(iv.length);
            buffer.put(iv);
            buffer.put(encryptedPrivateKey);

            Path filePath = getPrivateKeyPath(username);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, buffer.array());

        } catch (Exception e) {
            throw new RuntimeException("Could not save private key", e);
        }
    }

    public PrivateKey loadPrivateKey(String username, String password) {
        try {
            Path filePath = getPrivateKeyPath(username);
            byte[] fileBytes = Files.readAllBytes(filePath);

            ByteBuffer buffer = ByteBuffer.wrap(fileBytes);

            int saltLength = buffer.getInt();
            byte[] salt = new byte[saltLength];
            buffer.get(salt);

            int ivLength = buffer.getInt();
            byte[] iv = new byte[ivLength];
            buffer.get(iv);

            byte[] encryptedPrivateKey = new byte[buffer.remaining()];
            buffer.get(encryptedPrivateKey);

            SecretKey aesKey = deriveKey(password, salt);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));

            byte[] privateKeyBytes = cipher.doFinal(encryptedPrivateKey);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        } catch (Exception e) {
            throw new RuntimeException("Could not load private key", e);
        }
    }

    private SecretKey deriveKey(String password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, AES_KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private byte[] randomBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

    private Path getPrivateKeyPath(String username) throws IOException {
        Path appDir = Path.of(System.getProperty("user.home"), ".messagee2e", "keys");
        return appDir.resolve(username + "_private.key");
    }
}
