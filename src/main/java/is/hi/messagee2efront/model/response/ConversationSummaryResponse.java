package is.hi.messagee2efront.model.response;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class ConversationSummaryResponse {
    private String otherUsersUsername;
    private String lastMessageEncryptedContent;
    private String lastMessageSentAt;
    private boolean lastMessageRead;
    private boolean lastMessageSentByCurrentUser;
    private int unreadCount;

    public ConversationSummaryResponse() {
    }

    public String getOtherUsersUsername() {
        return otherUsersUsername;
    }

    public void setOtherUsersUsername(String otherUsersUsername) {
        this.otherUsersUsername = otherUsersUsername;
    }

    public String getLastMessageEncryptedContent() {
        return lastMessageEncryptedContent;
    }

    public void setLastMessageEncryptedContent(String lastMessageEncryptedContent) {
        this.lastMessageEncryptedContent = lastMessageEncryptedContent;
    }

    public String getLastMessageSentAt() {
        return lastMessageSentAt;
    }

    public void setLastMessageSentAt(String lastMessageSentAt) {
        this.lastMessageSentAt = lastMessageSentAt;
    }

    public boolean isLastMessageRead() {
        return lastMessageRead;
    }

    public void setLastMessageRead(boolean lastMessageRead) {
        this.lastMessageRead = lastMessageRead;
    }

    public boolean isLastMessageSentByCurrentUser() {
        return lastMessageSentByCurrentUser;
    }

    public void setLastMessageSentByCurrentUser(boolean lastMessageSentByCurrentUser) {
        this.lastMessageSentByCurrentUser = lastMessageSentByCurrentUser;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }
    @Override
    public String toString(){
        String unreadPart = unreadCount > 0 ? " | Unread: " + unreadCount : "";
        return otherUsersUsername + " | Last message at: " + lastMessageSentAt + unreadPart;
    }
}
