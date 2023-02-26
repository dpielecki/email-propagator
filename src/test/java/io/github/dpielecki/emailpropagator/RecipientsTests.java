package io.github.dpielecki.emailpropagator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.github.dpielecki.emailpropagator.recipients.Recipient;
import io.github.dpielecki.emailpropagator.recipients.RecipientRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipientsTests {

	private String appHost = "localhost";
	private static RestTemplate restTemplate = new RestTemplate();
	private URI uri;

	@LocalServerPort
	private int port;

	@Autowired
	private RecipientRepository recipientRepository;
	
	@BeforeEach
	void setup() {
		uri = URI.create("http://" + appHost + ":" + port + "/api/emails");
		recipientRepository.save(new Recipient("address1@example.com"));
		recipientRepository.save(new Recipient("address2@example.com"));
		recipientRepository.save(new Recipient("address3@example.com"));
	}

	@AfterEach
	void cleanup() {
		recipientRepository.deleteAll();
	}

	@Test
	void addsValidRecipient() {
		Recipient toAdd = new Recipient();
		toAdd.setAddress("address@example.com");

		Recipient added = restTemplate.postForObject(uri, toAdd, Recipient.class);

		assertNotNull(added);
	}

	@Test
	void throwsExceptionWhenAddingDuplicateAddress() {
		Recipient toAdd = new Recipient();
		toAdd.setAddress("address1@example.com");

		assertThrows(HttpClientErrorException.class, 
			() -> restTemplate.postForObject(uri, toAdd, Recipient.class)
		);
	}

	@TestFactory
	Stream<DynamicTest> throwsExceptionWhenAddingInvalidAddress() {
		return Stream.of(
			"@example.com",
			"address@",
			"",
			null,
			"   ",
			"..--..@exmample.com",
			"address@.example.com"
		).map(address ->
			DynamicTest.dynamicTest("Test add: " + address, () -> {
				Recipient toAdd = new Recipient();
				toAdd.setAddress(address);

				assertThrows(HttpClientErrorException.class, 
					() -> restTemplate.postForObject(uri, toAdd, Recipient.class)
				);
			})
		);
	}

	@Test
	void fetchesAllRecipients() {
		List result = restTemplate.getForObject(uri, List.class);

		assertNotNull(result);
		assertEquals(3, result.size());
	}

	@Test
	void fetchesRecipientById() {
		uri = URI.create(uri.toString() + "/" + recipientRepository.findAll().get(0).getId());

		assertNotNull(restTemplate.getForObject(uri, Recipient.class));
	}

	@Test 
	void throwsExceptionWhenFetchingNonexistentRecipient() {
		uri = URI.create(uri.toString() + "/0");

		assertThrows(HttpClientErrorException.class, 
			() -> restTemplate.getForObject(uri, Recipient.class)
		);
	}

	@Test
	void removesRecipientById() {
		uri = URI.create(uri.toString() + "/" + recipientRepository.findAll().get(0).getId());

		restTemplate.delete(uri);

		assertEquals(2, recipientRepository.findAll().size());
	}

	@Test
	void throwsExceptionWhenRemovingNonexistentRecipient() {
		uri = URI.create(uri.toString() + "/0");

		assertThrows(HttpClientErrorException.class, 
			() -> restTemplate.delete(uri)
		);
	}

	@Test
	void updatesRecipientWithValidAddress() {
		Long id = recipientRepository.findAll().get(0).getId();
		uri = URI.create(uri.toString() + "/" + id);
		Recipient toUpdate = new Recipient(id, "newaddress@example.com");

		restTemplate.put(uri, toUpdate);

		assertEquals(toUpdate, recipientRepository.findById(id).get());
	}

	@Test
	void throwsExceptionWhenUpdatingRecipientWithInvalidAddress() {
		uri = URI.create(uri.toString() + recipientRepository.findAll().get(0).getId());
		Recipient toUpdate = new Recipient("@example.com");

		assertThrows(HttpClientErrorException.class, 
			() -> restTemplate.put(uri, toUpdate)
		);
	}

	@Test
	void throwsExceptionWhenUpdatingRecipientWithDuplicateAddress() {
		uri = URI.create(uri.toString() + recipientRepository.findAll().get(0).getId());
		Recipient toUpdate = new Recipient("address2@example.com");

		assertThrows(HttpClientErrorException.class, 
			() -> restTemplate.put(uri, toUpdate)
		);
	}


	@Test
	void throwsExceptionWhenUpdatingWithMismatchingIds() {
		Long id = recipientRepository.findAll().get(0).getId();
		uri = URI.create(uri.toString() + id);
		Recipient toUpdate = new Recipient(id + 1l, "newaddress@example.com");

		assertThrows(HttpClientErrorException.class, 
			() -> restTemplate.put(uri, toUpdate)
		);
	}
}
