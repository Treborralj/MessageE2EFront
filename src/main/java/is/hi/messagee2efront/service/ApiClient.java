package is.hi.messagee2efront.service;

import java.net.http.HttpClient;
import java.time.Duration;

/******************************************************************************
 * @author Róbert A. Jack
 * Tölvupóstur: ral9@hi.is
 * Lýsing : 
 *
 *****************************************************************************/
public class ApiClient {
    private static final String BASE_URL = "https://messagee2e-production.up.railway.app";

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static String getBaseUrl(){
        return BASE_URL;
    }

    public static HttpClient getHttpClient(){
        return httpClient;
    }
}
