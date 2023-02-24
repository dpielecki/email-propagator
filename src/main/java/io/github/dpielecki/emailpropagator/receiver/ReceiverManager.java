package io.github.dpielecki.emailpropagator.receiver;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityExistsException;

@Service
public class ReceiverManager {

    @Autowired
    private final ReceiverRepository receiverRepository;

    public ReceiverManager(ReceiverRepository receiverRepository) {
        this.receiverRepository = receiverRepository;
    }

    public List<Receiver> getAll() {
        return receiverRepository.findAll();
    }

    public Receiver getOneById(Long id) {
        return receiverRepository.findById(id)
            .orElseThrow();
    }

    public Receiver addOne(Receiver receiver) {
        if (receiverRepository.existsByAddress(receiver.getAddress()))
            throw new EntityExistsException();
        return receiverRepository.save(receiver);
    }

    public Receiver updateOne(Long id, Receiver receiver) {
        receiverRepository.findById(id)
            .orElseThrow();
        return receiverRepository.save(receiver);
    }

    public void deleteOne(Long id) {
        receiverRepository.findById(id)
            .orElseThrow();
        receiverRepository.deleteById(id);
    }
}
