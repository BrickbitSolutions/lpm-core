package be.brickbit.lpm.mail;

public interface MailTemplateService {
    String createGeneralMessage(String name, String message);

    String createPasswordResetMessage(String name, String newPassword);

    String createActivationMail(String name, String token);
}
