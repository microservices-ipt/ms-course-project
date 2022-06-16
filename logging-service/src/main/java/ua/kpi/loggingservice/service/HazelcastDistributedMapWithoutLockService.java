package ua.kpi.loggingservice.service;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.kpi.sharedmodel.Message;

import java.util.Map;
import java.util.UUID;

@Service
//@Primary
@Slf4j
public class HazelcastDistributedMapWithoutLockService implements LoggingService {

    public HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    public Map<Integer, Integer> messages = hazelcastInstance.getMap("withoutLock_map");

    public Integer key = Integer.valueOf(1);

    public HazelcastDistributedMapWithoutLockService() {}

    @Override
    public String getMessages() {
        try {
            Integer result = messages.get(key);
            return result.toString();
        } catch (Exception e) {
            log.info(e.toString());
            return "0";
        }
    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST logging service message: {}", msg);
        try {
            for (int k = 0; k < 1000; k++) {
                Integer value = messages.get(key);
                Thread.sleep(10);
                value = value.intValue() + 1;
                messages.put(key, value);
            }
        } catch (Exception e) {
            messages.put(key, 0);
            log.info(e.toString());
            writeMessage(msg);
        }
    }
}