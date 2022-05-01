package ua.kpi.loggingservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.kpi.sharedmodel.Message;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/logging")
@Slf4j
public class LoggingController {

    public Map<UUID, String> messages = new ConcurrentHashMap<>();

    @GetMapping()
    public String getMessages() {
        return messages.values().toString();
    }

    @PostMapping()
    public void writeMessage(@RequestBody Message msg) {
        log.info("POST logging service message: {}", msg);
        messages.put(msg.getId(), msg.getText());
    }


}
