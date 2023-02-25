package io.github.dpielecki.emailpropagator.recipients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {
    boolean existsByAddress(String address);
}
