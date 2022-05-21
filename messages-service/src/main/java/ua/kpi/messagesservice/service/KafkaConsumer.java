package ua.kpi.messagesservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ua.kpi.sharedmodel.Message;

@Slf4j
@Service
public class KafkaConsumer {

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = "#{'${spring.consul.kafka.topic}'}")
    public void consume(Message message) {
        log.info("Consuming the message: {}", message);
        messageService.saveMessage(message);
    }
}
