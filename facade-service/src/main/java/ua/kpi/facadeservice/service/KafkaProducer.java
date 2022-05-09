package ua.kpi.facadeservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import ua.kpi.sharedmodel.Message;

@Service
@Slf4j
public class KafkaProducer {


    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String kafkaMessagesTopic;

    public void produce(Message message) {

        ListenableFuture<SendResult<String, Message>> future =
                kafkaTemplate.send(kafkaMessagesTopic, message.getId().toString(), message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, Message> result) {
                log.info("Sent message=[{}] with offset=[{}]" , message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable e) {
                log.info("Unable to send message=[{}] due to : {}" , message, e.getMessage());
            }
        });

    }

}
