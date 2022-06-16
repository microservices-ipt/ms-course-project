package ua.kpi.loggingservice.service;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.kpi.sharedmodel.Message;

import java.util.Map;

@Service
//@Primary
@Slf4j
public class HazelcastDistributedMapWithPessimisticLockService implements LoggingService {

    public HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    public IMap<Integer, Integer> messages = hazelcastInstance.getMap("pessimisticLock_map");

    public Integer key = Integer.valueOf(1);

    public HazelcastDistributedMapWithPessimisticLockService() {}

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
        for (int k = 0; k < 1000; k++) {
            messages.lock(key);
            try {
                Integer value = messages.get(key);
                Thread.sleep(10);
                value = value.intValue() + 1;
                messages.put(key, value);
            } catch (Exception e) {
                messages.put(key, 0);
                log.info(e.toString());
                writeMessage(msg);
            } finally {
                messages.unlock(key);
            }
        }
    }
}
