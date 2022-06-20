package ua.kpi.loggingservice.service;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.kpi.sharedmodel.Message;

import java.util.Map;

@Service
//@Primary
@Slf4j
public class HazelcastDistributedMapWithOptimisticLockService implements LoggingService {

    public HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    public Map<Integer, Integer> messages = hazelcastInstance.getMap("optimisticLock_map");

    public Integer key = 1;

    public HazelcastDistributedMapWithOptimisticLockService() {
        messages.putIfAbsent(key, 0);
    }

    @Override
    public String getMessages() {
        Integer result = messages.get(key);
        return result.toString();

    }

    @Override
    public void writeMessage(Message msg) {
        log.info("POST logging service message: {}", msg);
        for (int k = 0; k < 1000; k++) {
            writeIteration();
        }
    }

    private void writeIteration() {
        try {
            for (; ; ) {
                Integer oldValue = messages.get(key);
                Integer newValue = oldValue;
                Thread.sleep(10);
                newValue = newValue + 1;
                if (messages.replace(key, oldValue, newValue))
                    break;
            }
        } catch (Exception e) {
            log.info(e.toString());
            writeIteration();
        }

    }
}
