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
//@Primary
@Slf4j
public class HazelcastDistributedMapService implements LoggingService {

    public HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    public Map<Integer, Integer> messages = hazelcastInstance.getMap("distributed_map");

    public HazelcastDistributedMapService() {}

    @Override
    public String getMessages() {
        return messages.values().toString();
    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST logging service message: {}", msg);
        for (int i = 0; i < 1000; i++) {
            messages.put(Integer.valueOf(i), Integer.valueOf(i));
        }
    }
}
