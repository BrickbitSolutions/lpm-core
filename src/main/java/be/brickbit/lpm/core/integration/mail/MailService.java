package be.brickbit.lpm.core.integration.mail;

public interface MailService {
    void sendMail(String to, String subject, String message);
}
