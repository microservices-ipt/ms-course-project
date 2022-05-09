package ua.kpi.loggingservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.kpi.loggingservice.service.LoggingService;
import ua.kpi.sharedmodel.Message;

@RestController
@RequestMapping("/logging")
@Slf4j
public class LoggingController {

    public LoggingService loggingService;

    public LoggingController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }


    @GetMapping()
    public String getMessages() {
        return loggingService.getMessages();
    }

    @PostMapping()
    public void writeMessage(@RequestBody Message msg) {
        loggingService.writeMessage(msg);
    }


}
