package is.hi.messagee2efront.model.request;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class SendMessageRequest {
    private String receiverUsername;
    private String encryptedContent;

    public SendMessageRequest() {
    }

    public SendMessageRequest(String receiverUsername, String encryptedContent) {
        this.receiverUsername = receiverUsername;
        this.encryptedContent = encryptedContent;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getEncryptedContent() {
        return encryptedContent;
    }

    public void setEncryptedContent(String encryptedContent) {
        this.encryptedContent = encryptedContent;
    }
}
