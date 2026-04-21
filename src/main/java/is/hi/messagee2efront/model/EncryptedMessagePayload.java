package is.hi.messagee2efront.model;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class EncryptedMessagePayload {
    private String encryptedAesKeyForSender;
    private String encryptedAesKeyForReceiver;
    private String iv;
    private String ciphertext;

    public EncryptedMessagePayload() {
    }

    public EncryptedMessagePayload(String encryptedAesKeyForSender,
                                   String encryptedAesKeyForReceiver,
                                   String iv,
                                   String ciphertext) {
        this.encryptedAesKeyForSender = encryptedAesKeyForSender;
        this.encryptedAesKeyForReceiver = encryptedAesKeyForReceiver;
        this.iv = iv;
        this.ciphertext = ciphertext;
    }

    public String getEncryptedAesKeyForSender() {
        return encryptedAesKeyForSender;
    }

    public void setEncryptedAesKeyForSender(String encryptedAesKeyForSender) {
        this.encryptedAesKeyForSender = encryptedAesKeyForSender;
    }

    public String getEncryptedAesKeyForReceiver() {
        return encryptedAesKeyForReceiver;
    }

    public void setEncryptedAesKeyForReceiver(String encryptedAesKeyForReceiver) {
        this.encryptedAesKeyForReceiver = encryptedAesKeyForReceiver;
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
