package ua.kpi.loggingservice.service;

import com.hazelcast.collection.IQueue;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ua.kpi.sharedmodel.Message;

import java.util.LinkedList;

@Service
@Primary
@Slf4j
public class HazelcastQueueService implements LoggingService {

    public HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
    public IQueue<Integer> messages = hazelcastInstance.getQueue("queue");


    @Override
    public String getMessages() {
        LinkedList<Integer> result = new LinkedList<>();
        while ( true ) {
            try {
                int item = messages.take();
                System.out.println("Consumed: " + item);
                result.push(item);
                if (item == -1) {
                    messages.put(-1);
                    break;
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                log.info(e.toString());
            }
        }
        return result.toString();
    }

    @Override
    public void writeMessage(Message msg) {
        try{
            for ( int k = 1; k < 10; k++ ) {
                messages.put( k );
                System.out.println( "Producing: " + k );
                Thread.sleep(1000);
            }
            messages.put( -1 );
            System.out.println( "Producer Finished!" );
        } catch (Exception e) {
            log.info(e.toString());
        }

    }
}
