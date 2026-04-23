package is.hi.messagee2efront.model;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: View model used to display decrypted messages in the conversation UI.
 *
 *****************************************************************************/
public class DecryptedMessageViewModel {
    private final String senderUsername;
    private final String plaintext;
    private final String formattedTime;
    private final boolean sentByCurrentUser;

    public DecryptedMessageViewModel(String senderUsername,
                                     String plaintext,
                                     String formattedTime,
                                     boolean sentByCurrentUser
    ) {
        this.senderUsername = senderUsername;
        this.plaintext = plaintext;
        this.formattedTime = formattedTime;
        this.sentByCurrentUser = sentByCurrentUser;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public boolean isSentByCurrentUser() {
        return sentByCurrentUser;
    }
}
