package is.hi.messagee2efront.service;

import java.security.PrivateKey;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Stores temporary session information for the currently logged-in
 *              user.
 *
 *****************************************************************************/
public class SessionStorage {
    private static String username;
    private static PrivateKey privateKey;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        SessionStorage.username = username;
    }

    public static PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static void setPrivateKey(PrivateKey privateKey) {
        SessionStorage.privateKey = privateKey;
    }

    /**
     * Clears the stored session data.
     */
    public static void clear() {
        username = null;
        privateKey = null;
    }
}
