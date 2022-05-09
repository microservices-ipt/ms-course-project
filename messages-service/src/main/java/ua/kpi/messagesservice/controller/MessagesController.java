package ua.kpi.messagesservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.messagesservice.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    @Autowired
    private MessageService messageService;

    @GetMapping()
    public String messages() {
        return messageService.getMessages();
    }
}
