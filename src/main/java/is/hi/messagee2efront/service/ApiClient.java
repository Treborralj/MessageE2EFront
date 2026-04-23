package is.hi.messagee2efront.service;

import java.net.http.HttpClient;
import java.time.Duration;

/******************************************************************************
 * @author Róbert A. Jack
 * e-mail: ral9@hi.is
 * Description: Provides shared HTTP client configuration and backend API settings.
 *
 *****************************************************************************/
public class ApiClient {
    private static final String BASE_URL = "https://messagee2e-production.up.railway.app";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Returns the base URL of the backend API.
     */
    public static String getBaseUrl(){
        return BASE_URL;
    }

    /**
     * Returns the shared HTTP client used for backend requests.
     * @return
     */
    public static HttpClient getHttpClient(){
        return httpClient;
    }
}
