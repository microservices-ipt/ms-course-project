package ua.kpi.facadeservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.kpi.facadeservice.service.FacadeService;


@RestController
@RequestMapping("/facade")
@Slf4j
public class FacadeController {

    private final FacadeService facadeService;

    public FacadeController(FacadeService facadeService) {
        this.facadeService = facadeService;
    }

    @GetMapping()
    public String getMessages() {
        return facadeService.getMessages();
    }

    @PostMapping()
    public void writeMessage(@RequestBody String body) {
        facadeService.writeMessage(body);
    }

}
