package ua.kpi.loggingservice.controller;

import org.springframework.http.ResponseEntity;
import ua.kpi.loggingservice.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
//@RequestMapping("/api/v1")
public class LoggingController {

    Logger logger = LoggerFactory.getLogger(LoggingController.class);

    public Map<UUID, String> messages = new ConcurrentHashMap<>();

    @GetMapping("/hello")
    public String helloWorld(){
        return "world!";
    }

    @GetMapping("/log")
    public String getMessages() {
        return messages.values().toString();
    }

    @PostMapping("/log")
    public ResponseEntity<Void> writeMessage(@RequestBody Message msg) {
        logger.info(msg.toString());
        messages.put(msg.id, msg.text);
        return ResponseEntity.ok().build();
    }




}
