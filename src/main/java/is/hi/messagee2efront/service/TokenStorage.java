package is.hi.messagee2efront.service;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Stores the JWT token used for authenticated backend requests.
 *
 *****************************************************************************/
public class TokenStorage {
    private static String token;

    public static void setToken(String newtoken) {
        token = newtoken;
    }

    public static String getToken() {
        return token;
    }

    /**
     * Removes the stored authentication token.
     */
    public static void clearToken() {
        token = null;
    }
}
