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

    public Integer key = Integer.valueOf(1);

    public HazelcastDistributedMapWithOptimisticLockService() {}

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
            for ( int k = 0; k < 1000; k++ ) {
                for (; ; ) {
                    Integer oldValue = messages.get( key );
                    Integer newValue = oldValue;
                    Thread.sleep( 10 );
                    newValue = newValue + 1;
                    if ( messages.replace( key, oldValue, newValue ) )
                        break;
                }
            }
        } catch (Exception e) {
            messages.put( key, 0);
            log.info(e.toString());
            writeMessage(msg);
        }
    }
}
