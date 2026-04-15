package is.hi.messagee2efront.service;

import com.fasterxml.jackson.databind.ser.Serializers;

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
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class CryptoService {
    public SecretKey generateAesKey(){
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (Exception e){
            throw new RuntimeException("Faild to generate AES key", e);
        }
    }

    public byte[] generateIv(){
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public String encryptPlainText(String plaintext, SecretKey aesKey, byte[] iv){
        try{
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch(Exception e){
            throw new RuntimeException("Failed to encrypt plaintext");
        }
    }

    public String decryptCipherText(String ciphertext, SecretKey aesKey, byte[] iv){
        try{
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch(Exception e){
            throw new RuntimeException("Failed to decrypt ciphertext", e);
        }
    }

    public String encryptAesKey(SecretKey aesKey, PublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(aesKey.getEncoded());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e){
            throw new RuntimeException("Failed to encrypt AES key", e);
        }
    }

    public SecretKey decryptAesKey(String encryptedAesKey, PrivateKey privatekey){
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privatekey);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedAesKey));
            return new SecretKeySpec(decrypted, "AES");
        } catch(Exception e){
            throw new RuntimeException("Failed to decrypt AES key", e);
        }
    }

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

    public String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public byte[] decodeBase64(String base64) {
        return Base64.getDecoder().decode(base64);
    }
}
