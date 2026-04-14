package is.hi.messagee2efront.service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class KeyService {
    public KeyPair generateRsaKeyPair(){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Could not generate RSA key pair", e);
        }
    }
    public String publicKeyToBase64(KeyPair keyPair){
        return Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }
}
