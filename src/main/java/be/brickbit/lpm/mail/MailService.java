package be.brickbit.lpm.mail;

public interface MailService {
    void sendMail(String to, String subject, String message);
}
