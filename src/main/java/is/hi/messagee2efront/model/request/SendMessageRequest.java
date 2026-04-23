package is.hi.messagee2efront.model.request;

import is.hi.messagee2efront.model.EncryptedMessagePayload;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Request object sent to the backend when sending a message.
 *
 *****************************************************************************/
public class SendMessageRequest {
    private String receiverUsername;
    private String encryptedAesKeyForSender;
    private String encryptedAesKeyForReceiver;
    private String iv;
    private String ciphertext;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String receiverUsername,
                              String encryptedAesKeyForSender,
                              String encryptedAesKeyForReceiver,
                              String iv,
                              String ciphertext) {
        this.receiverUsername = receiverUsername;
        this.encryptedAesKeyForSender = encryptedAesKeyForSender;
        this.encryptedAesKeyForReceiver = encryptedAesKeyForReceiver;
        this.iv = iv;
        this.ciphertext = ciphertext;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
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
