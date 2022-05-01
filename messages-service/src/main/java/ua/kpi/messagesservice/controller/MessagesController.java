package ua.kpi.messagesservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessagesController {

    @GetMapping()
    public String message() {
        return "not implemented yet";
    }
}
