package io.github.dpielecki.emailpropagator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.dpielecki.emailpropagator.propagator.Message;
import io.github.dpielecki.emailpropagator.propagator.PropagatorService;
import io.github.dpielecki.emailpropagator.recipients.Recipient;
import io.github.dpielecki.emailpropagator.recipients.RecipientRepository;
import jakarta.mail.MessagingException;

@SpringBootTest
public class PropagatorTests {

	@Autowired
	private RecipientRepository recipientRepository;

    @Autowired
    private PropagatorService propagatorService;
	
	@BeforeEach
	void setup() {
		recipientRepository.save(new Recipient("address1@example.com"));
		recipientRepository.save(new Recipient("address2@example.com"));
		recipientRepository.save(new Recipient("address3@example.com"));
	}

	@AfterEach
	void cleanup() {
		recipientRepository.deleteAll();
	}

	@Test
	void preparesValidMimeMessage() throws MessagingException {
        Message message = new Message("Subject", "<h1>Some text<h1/>");
        
        assertNotNull(propagatorService.prepareMimeMessage(message));
	}

	@TestFactory
	Stream<DynamicTest> throwsExceptionWhenPreparingInvalidMessage() {
		return Stream.of(
			new Message("Subject", null),
            new Message(null, "Some text.")
		).map(message ->
			DynamicTest.dynamicTest("Test prepare: " + message, () -> {
				assertThrows(IllegalArgumentException.class, 
					() -> propagatorService.prepareMimeMessage(message)
				);
			})
		);
	}
}
