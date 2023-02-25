package io.github.dpielecki.emailpropagator.recipients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;

@Service
public class RecipientManager {

    @Autowired
    private final RecipientRepository recipientRepository;

    public RecipientManager(RecipientRepository recipientRepository) {
        this.recipientRepository = recipientRepository;
    }

    public List<Recipient> getAll() {
        return recipientRepository.findAll();
    }

    public Recipient getOneById(Long id) {
        return recipientRepository.findById(id)
            .orElseThrow();
    }

    public Recipient addOne(Recipient recipient) {
        if (recipientRepository.existsByAddress(recipient.getAddress()))
            throw new EntityExistsException();
        return recipientRepository.save(recipient);
    }

    public Recipient updateOne(Long id, Recipient recipient) {
        recipientRepository.findById(id)
            .orElseThrow();
        return recipientRepository.save(recipient);
    }

    public void deleteOne(Long id) {
        recipientRepository.findById(id)
            .orElseThrow();
        recipientRepository.deleteById(id);
    }
}
