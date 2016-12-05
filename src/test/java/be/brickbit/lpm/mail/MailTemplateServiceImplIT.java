package be.brickbit.lpm.mail;

import be.brickbit.lpm.core.AbstractControllerIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;

public class MailTemplateServiceImplIT extends AbstractControllerIT{
    @Autowired
    private MailTemplateService mailTemplateService;

    @Test
    public void createsPasswordChangeMail() throws Exception {
        final String name = "Drunk'n Jay";
        final String newPassword = "n3wpwd";
        String result = mailTemplateService.createPasswordResetMessage(name, newPassword);

        assertThat(result).contains(name);
        assertThat(result).contains(newPassword);
    }

    @Test
    public void createsGeneralMessageMail() throws Exception {
        final String name = "Drunk'n Jay";
        final String message = "I Wanna Set The Universe On Fire <3";
        String result = mailTemplateService.createGeneralMessage(name, message);

        assertThat(result).contains(name);
        assertThat(result).contains(message);
    }

    @Test
    public void createsActivationMail() throws Exception {
        final String name = "Drunk'n Jay";
        final String token = randomString();
        String result = mailTemplateService.createActivationMail(name, token);

        assertThat(result).contains(name);
        assertThat(result).contains(token);
    }
}