package ua.kpi.facadeservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.kpi.sharedmodel.Message;

import java.util.UUID;

@RestController
@RequestMapping("/facade")
@Slf4j
public class FacadeController {

    WebClient loggingWebClient = WebClient.create("http://localhost:8081");
    WebClient messagesWebClient = WebClient.create("http://localhost:8082");

    @GetMapping("/hello")
    public String helloWorld() {
        return "world!";
    }

    @GetMapping()
    public String getMessages() {
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

    @PostMapping()
    public void writeMessage(@RequestBody String body) {
        var msg = new Message(UUID.randomUUID(), body);
        log.info("Message {}", msg);
        loggingWebClient.post()
                .uri("/logging")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(msg), Message.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

}
