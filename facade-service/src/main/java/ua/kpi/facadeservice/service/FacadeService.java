package ua.kpi.facadeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private final KafkaProducer kafkaProducer;

    private final List<WebClient> loggingWebClients;
    private final List<WebClient> messagesWebClients;

    public FacadeService(KafkaProducer kafkaProducer,
                         @Value("${external.service.logging.node1}") String loggingServiceNode1,
                         @Value("${external.service.logging.node2}") String loggingServiceNode2,
                         @Value("${external.service.logging.node3}") String loggingServiceNode3,
                         @Value("${external.service.messages.node1}") String messagesServiceNode1,
                         @Value("${external.service.messages.node2}") String messagesServiceNode2
                         ) {
        this.kafkaProducer = kafkaProducer;
        loggingWebClients = List.of(
                WebClient.create(loggingServiceNode1),
                WebClient.create(loggingServiceNode2),
                WebClient.create(loggingServiceNode3));
        messagesWebClients = List.of(
                WebClient.create(messagesServiceNode1),
                WebClient.create(messagesServiceNode2));

    }

    private final String LOGGING_URL = "/logging";
    private final String MESSAGES_URL = "/messages";
    private final Random random = new Random();

    public String getMessages() {
        var loggingWebClient = getRandomService(loggingWebClients);
        var messagesWebClient = getRandomService(messagesWebClients);
        var loggingResponse = getMessages(loggingWebClient, LOGGING_URL);
        var messageResponse = getMessages(messagesWebClient, MESSAGES_URL);
        return loggingResponse + " : " + messageResponse;
    }

    public void writeMessage(@RequestBody String body) {
        var msg = new Message(UUID.randomUUID(), body);
        log.info("Message {}", msg);

        var loggingWebClient = getRandomService(loggingWebClients);
        postMessage(loggingWebClient, LOGGING_URL, msg);

        kafkaProducer.produce(msg);
    }

    private void postMessage(WebClient serviceWebClient, String uri, Message msg){
        serviceWebClient
                .post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(msg), Message.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    private String getMessages(WebClient serviceWebClient, String uri){
        return serviceWebClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private WebClient getRandomService(List<WebClient> webClients) {
        int randomNumber = random.nextInt(webClients.size());
        log.info("Random number: {}", randomNumber);
        return webClients.get(randomNumber);
    }

}
