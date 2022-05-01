package ua.kpi.facadeservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ua.kpi.facadeservice.model.Message;

import java.util.UUID;

@RestController
//@RequestMapping("/api/v1")
public class FacadeController {

    WebClient loggingWebClient = WebClient.create("http://localhost:8081");
    WebClient messagesWebClient = WebClient.create("http://localhost:8082");

    @GetMapping("/hello")
    public String helloWorld() {
        return "world!";
    }

    @GetMapping("/facade")
    public String getMessages() {
        var loggingResponce = loggingWebClient
                .get()
                .uri("/log")
                .retrieve()
                .bodyToMono(String.class);
        var messageResponce = messagesWebClient
                .get()
                .uri("/message")
                .retrieve()
                .bodyToMono(String.class);
        return loggingResponce.block() + " : " + messageResponce.block();
    }

    @PostMapping("/facade")
    public Mono<Void> writeMessage(@RequestBody String body) {
        UUID id = UUID.randomUUID();
        var msg = new Message(id, body);
        System.out.println("id: " + id);
        System.out.println("body: "+ body);
        System.out.println("Message " + msg);
        return loggingWebClient
                .post()
                .uri("/log")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(msg), Message.class)
                .retrieve()
                .bodyToMono(Void.class);
    }

}
