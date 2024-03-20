package ru.crpt.ismp.CrptApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrptApi {
    private final LocalDateTime timer;
    private int possibleRequests;
    private final String URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.possibleRequests = requestLimit;
        this.timer = LocalDateTime.now().plusSeconds(timeUnit.getSeconds());
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
        log.info("Constructed new CrtpApi: request limit: {}, time is up to: {}.",
                requestLimit , timer.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public void createDocument(Object document, String sign) throws IOException, InterruptedException {
        if (LocalDateTime.now().isAfter(timer)) {
            log.info("Time is out, request will not be send.");
            return;
        } else if (possibleRequests <= 0) {
            log.info("Request limit is out, request will not be send.");
            return;
        } else {
            possibleRequests -= 1;
            log.info("Preparing to send request, possible requests left: {}.", possibleRequests);
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(document)))
                .header("X-SIGN", sign)
                .build();
        log.info("Sending request to URL {} with sign {}.", URL, sign);
        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
        log.info("Response status code: {}, body: {}.", response.statusCode(), response.body());
    }
}
