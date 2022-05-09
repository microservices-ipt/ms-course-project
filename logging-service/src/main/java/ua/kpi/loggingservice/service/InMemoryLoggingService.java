package ua.kpi.loggingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ua.kpi.sharedmodel.Message;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
//@Primary
@Slf4j
public class InMemoryLoggingService implements LoggingService {
    public Map<UUID, String> messages = new ConcurrentHashMap<>();

    public InMemoryLoggingService() {

    }

    @Override
    public String getMessages() {
        return messages.values().toString();
    }

    @Override
    public void writeMessage(@RequestBody Message msg) {
        log.info("POST logging service message: {}", msg);
        messages.put(msg.getId(), msg.getText());
    }
}
