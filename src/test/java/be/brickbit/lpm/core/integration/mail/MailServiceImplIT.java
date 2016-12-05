package be.brickbit.lpm.core.integration.mail;

import be.brickbit.lpm.core.AbstractControllerIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static be.brickbit.lpm.core.util.WiserAssertions.assertReceivedMessage;

public class MailServiceImplIT extends AbstractControllerIT {
    @Autowired
    private MailService mailService;

    @Test
    public void sendsEmail() throws Exception {
        mailService.sendMail("knights@ni.com", "Test", "Test");

        assertReceivedMessage(wiser())
                .from("me@localhost")
                .to("knights@ni.com")
                .withSubject("[LPM] Test");
    }
}