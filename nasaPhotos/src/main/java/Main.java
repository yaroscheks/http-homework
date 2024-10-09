import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

public class Main {

    public static final String REMOTE_SERVICE_URI = "https://api.nasa.gov/planetary/apod?api_key=qVPqVCWAqtfMMNNKWyFsIGRTvaO781LEbHZ2v7CA";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My Test Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        // создание объекта запроса с произвольными заголовками
        HttpGet request = new HttpGet(REMOTE_SERVICE_URI);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        // отправка запроса
        CloseableHttpResponse response = httpClient.execute(request);

        // вывод заголовков
        Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
        System.out.println();

        // чтение тела ответа
        Post post = mapper.readValue(response.getEntity().getContent(), Post.class);
        try {
            FileUtils.copyURLToFile(new URL(post.getUrl()), new File("saved-photo.jpg"));
            System.out.println("Image downloaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
