package io.github.dpielecki.emailpropagator.recipients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/emails")
public class RecipientController {

    @Autowired
    private final RecipientManager recipientManager;

    public RecipientController(RecipientManager recipientManager) {
        this.recipientManager = recipientManager;
    }

    @GetMapping
    public List<Recipient> getAll() {
        return recipientManager.getAll();
    }

    @GetMapping("/{id}")
    public Recipient getOne(@PathVariable @Parameter(name = "id") Long id) {
        return recipientManager.getOneById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Recipient addOne(@RequestBody Recipient recipient) {
        return recipientManager.addOne(recipient);
    }

    @PutMapping("/{id}")
    public Recipient updateOne(@PathVariable @Parameter(name = "id") Long id, @RequestBody Recipient recipient) {
        if (recipient.getId() != id) 
            throw new IllegalArgumentException("Given IDs didn't match.");
        return recipientManager.updateOne(id, recipient);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable @Parameter(name = "id") Long id) {
        recipientManager.deleteOne(id);
    }
}