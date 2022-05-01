package ua.kpi.messagesservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v1")
public class MessagesController {

    @GetMapping("/hello")
    public String helloWorld(){
        return "world!";
    }

    @GetMapping("/message")
    public String message() {
        return "not implemented yet";
    }
}
