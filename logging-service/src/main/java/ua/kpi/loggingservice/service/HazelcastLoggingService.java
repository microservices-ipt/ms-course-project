package ua.kpi.loggingservice.service;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.kpi.sharedmodel.Message;

import java.util.Map;
import java.util.UUID;

@Service
@Primary
@Slf4j
public class HazelcastLoggingService implements LoggingService {

    public HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    public Map<UUID, String> messages = hazelcastInstance.getMap("logging_map");

    public HazelcastLoggingService() {

    }

    @Override
    public String getMessages() {
        return messages.values().toString();
    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST logging service message: {}", msg);
        messages.put(msg.getId(), msg.getText());
    }
}
