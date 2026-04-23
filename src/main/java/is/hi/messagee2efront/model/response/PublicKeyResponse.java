package is.hi.messagee2efront.model.response;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Response object returned after requesting for a user's public key.
 *
 *****************************************************************************/
public class PublicKeyResponse {
    private String username;
    private String publicKey;

    public PublicKeyResponse() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
