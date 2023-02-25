package io.github.dpielecki.emailpropagator.propagator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import io.github.dpielecki.emailpropagator.recipients.Recipient;
import io.github.dpielecki.emailpropagator.recipients.RecipientManager;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class PropagatorService {
    
    private final JavaMailSender sender;
    private final RecipientManager recipientManager;

    @Autowired
    public PropagatorService(JavaMailSender sender, RecipientManager recipientManager) {
        this.sender = sender;
        this.recipientManager = recipientManager;
    }

    public MimeMessage prepareMimeMessage(Message message) throws MessagingException {

        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper =  new MimeMessageHelper(mimeMessage);

        helper.setSubject(message.getSubject());
        helper.setText(message.getText(), true);

        List<Recipient> recipients = recipientManager.getAll();
        if (recipients.size() == 0)
            throw new MessagingException("No recipients in the database.");

        helper.setTo(recipients
            .stream()
            .map(recipient -> recipient.getAddress())
            .toArray(String[]::new)
        );
            
        return mimeMessage;
    }

    public void propagate(Message message) throws MessagingException {        
        sender.send(prepareMimeMessage(message));
    }
}
