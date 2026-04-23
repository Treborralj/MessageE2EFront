package is.hi.messagee2efront.service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Descryption: This class provides cryptographic operations used for key handling,
 *              encryption and decryption.
 *
 *****************************************************************************/
public class CryptoService {
    /**
     * Generates a new AES key for message encryption.
     * @return a newly generated AES secret key
     */
    public SecretKey generateAesKey(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (Exception e){
            throw new RuntimeException("Faild to generate AES key", e);
        }
    }

    /**
     * Generates a random initialization vector for AES-GCM.
     * @return a randomly generated IV as a byte array
     */
    public byte[] generateIv(){
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * Encrypts plaintext using AES-GCM
     * @param plaintext the message to encrypt
     * @param aesKey the AES key used for encryption
     * @param iv the initialization vector used during ecnryption
     * @return the encrypted message encoded as Base64
     */
    public String encryptPlainText(String plaintext, SecretKey aesKey, byte[] iv){
        try{
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch(Exception e){
            throw new RuntimeException("Failed to encrypt plaintext");
        }
    }

    /**
     * Decrypts AES-GCM ciphertext into plaintext.
     * @param ciphertext the encrypted message encoded as Base64
     * @param aesKey the AES key used for decryption
     * @param iv the initialization vector used during encryption
     * @return the decrypted plaintext message
     */
    public String decryptCipherText(String ciphertext, SecretKey aesKey, byte[] iv){
        try{
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
            byte[] decrypted = cipher.doFinal(Base64.getUrlDecoder().decode(ciphertext));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch(Exception e){
            throw new RuntimeException("Failed to decrypt ciphertext", e);
        }
    }

    /**
     * Encrypts the AES key using and RSA public key.
     * @param aesKey the AES key to encrypt
     * @param publicKey the RSA public key of the recipient
     * @return the encrypted AES key encoded as Base64
     */
    public String encryptAesKey(SecretKey aesKey, PublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(aesKey.getEncoded());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e){
            throw new RuntimeException("Failed to encrypt AES key", e);
        }
    }

    /**
     * Decrypts an AES key using an RSA private key.
     * @param encryptedAesKey the encrypted AES key encoded as Base64
     * @param privateKey the RSA private key used for decryption
     * @return the decrypted AES key
     */
    public SecretKey decryptAesKey(String encryptedAesKey, PrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = cipher.doFinal(Base64.getUrlDecoder().decode(encryptedAesKey));
            return new SecretKeySpec(decrypted, "AES");
        } catch(Exception e){
            throw new RuntimeException("Failed to decrypt AES key", e);
        }
    }

    /**
     * Converts a Base64 encoded public key string into an RSA PublicKey object.
     * @param publicKey the Base64 encoded public key
     * @return the parsed RSE public key
     */
    public PublicKey parsePublicKey(String publicKey){
        try{
            byte[] keyBytes = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch(Exception e){
            throw new RuntimeException("Failed to pars public key", e);
        }
    }

    /**
     * Encodes raw bytes as a Base64 String
     */
    public String encodeBase64(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Decodes a Base64 string into raw bytes.
     */
    public byte[] decodeBase64(String base64) {
        return Base64.getUrlDecoder().decode(base64);
    }
}
