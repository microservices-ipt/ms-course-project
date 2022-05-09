package ua.kpi.loggingservice.service;

import org.springframework.web.bind.annotation.RequestBody;
import ua.kpi.sharedmodel.Message;


public interface LoggingService {

    String getMessages();

    void writeMessage(@RequestBody Message msg);
}
