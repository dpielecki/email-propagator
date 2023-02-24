package io.github.dpielecki.emailpropagator.receiver;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    boolean existsByAddress(String address);
}
