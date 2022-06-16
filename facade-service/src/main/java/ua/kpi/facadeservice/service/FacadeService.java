package ua.kpi.facadeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.kpi.sharedmodel.Message;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class FacadeService {

    public List<WebClient> loggingWebClients;
    public WebClient messagesWebClient;

    public FacadeService() {
        loggingWebClients = List.of(
                WebClient.create("http://localhost:8082"),
                WebClient.create("http://localhost:8083"),
                WebClient.create("http://localhost:8084")
        );
        messagesWebClient = WebClient.create("http://localhost:8085");
    }

    public String helloWorld() {
        return "world!";
    }

    public String getMessages() {
        int randomNumber = new Random().nextInt(loggingWebClients.size());
        log.info("random number: {}", randomNumber);
        var loggingWebClient = loggingWebClients.get(randomNumber);
        var loggingResponse = loggingWebClient
                .get()
                .uri("/logging")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        var messageResponse = messagesWebClient
                .get()
                .uri("/message")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return loggingResponse + " : " + messageResponse;
    }

    public void writeMessage(@RequestBody String body) {
        int randomNumber = new Random().nextInt(loggingWebClients.size());
        log.info("random number: {}", randomNumber);
        var msg = new Message(UUID.randomUUID(), body);
        log.info("Message {}", msg);
        var loggingWebClient = loggingWebClients.get(randomNumber);
        loggingWebClient.post()
                .uri("/logging")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(msg), Message.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
