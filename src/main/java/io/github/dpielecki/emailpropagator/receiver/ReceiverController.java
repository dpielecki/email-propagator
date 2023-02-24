package io.github.dpielecki.emailpropagator.receiver;

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

@RestController
@RequestMapping("/api/emails")
public class ReceiverController {

    @Autowired
    private final ReceiverManager receiverManager;

    public ReceiverController(ReceiverManager receiverManager) {
        this.receiverManager = receiverManager;
    }

    @GetMapping
    public List<Receiver> getAll() {
        return receiverManager.getAll();
    }

    @GetMapping("/{id}")
    public Receiver getOne(@PathVariable Long id) {
        return receiverManager.getOneById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Receiver addOne(@RequestBody Receiver receiver) {
        return receiverManager.addOne(receiver);
    }

    @PutMapping("/{id}")
    public Receiver updateOne(@PathVariable Long id, @RequestBody Receiver receiver) {
        if (receiver.getId() != id) 
            throw new IllegalArgumentException("Given IDs didn't match.");
        return receiverManager.updateOne(id, receiver);
    }

    @DeleteMapping("/{id}")
    public void deleteOne(@PathVariable Long id) {
        receiverManager.deleteOne(id);
    }
}