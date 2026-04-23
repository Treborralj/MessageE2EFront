package is.hi.messagee2efront.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Handles RSA key pair generation and public key conversion.
 *
 *****************************************************************************/
public class KeyService {
    /**
     * Generate a new RSA key pair for a user.
     * @return a newly generated RSA key pair.
     */
    public KeyPair generateRsaKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not generate RSA key pair", e);
        }
    }

    /**
     * Converts the public key from a KeyPair into Base64 format.
     * @param keyPair the RSA key pair containing the public key
     * @return the public key encoded as Base64
     */
    public String publicKeyToBase64(KeyPair keyPair) {
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }
}
