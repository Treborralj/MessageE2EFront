package is.hi.messagee2efront.model.response;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Response object returned after successful authentication.
 *
 *****************************************************************************/
public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
