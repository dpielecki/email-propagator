package io.github.dpielecki.emailpropagator.propagator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.mail.MessagingException;

@RestController
public class PropagatorController {
    
    private final PropagatorService propagatorService;

    @Autowired
    public PropagatorController(PropagatorService propagatorService) {
        this.propagatorService = propagatorService;
    }

    @PostMapping("/api/propagate")
    public String propagate(@RequestBody Message message) throws MessagingException {
        propagatorService.propagate(message);
        return "Message sent.";
    }
}
