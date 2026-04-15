package is.hi.messagee2efront.model;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class EncryptedMessagePayload {
    private String encryptedAesKey;
    private String iv;
    private String ciphertext;

    public EncryptedMessagePayload() {
    }

    public EncryptedMessagePayload(String encryptedAesKey, String iv, String ciphertext) {
        this.encryptedAesKey = encryptedAesKey;
        this.iv = iv;
        this.ciphertext = ciphertext;
    }

    public String getEncryptedAesKey() {
        return encryptedAesKey;
    }

    public void setEncryptedAesKey(String encryptedAesKey) {
        this.encryptedAesKey = encryptedAesKey;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
}
