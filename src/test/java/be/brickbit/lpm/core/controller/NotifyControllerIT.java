package be.brickbit.lpm.core.controller;

import org.junit.Test;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.controller.command.notify.NotifyCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static be.brickbit.lpm.core.util.WiserAssertions.assertReceivedMessage;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NotifyControllerIT extends AbstractControllerIT {
    @Test
    public void sendsTestMail() throws Exception {
        String mail = "knights@ni.co.uk";

        performPut("/notify/mail/test?mail=" + mail, null)
                .andExpect(status().isOk());

        assertReceivedMessage(wiser())
                .from("me@localhost")
                .to(mail)
                .withSubject("[LPM] Test Mail");
    }

    @Test
    public void notifiesUser() throws Exception {
        User user = UserFixture.mutable();
        user.setMobileNr("0470540890");

        insert(user);

        //TODO: Default SMS Service returns 404 -- Difficult to test due to integrations :/ -- Solution needed..
        performPost("/notify", new NotifyCommand(user.getId(), randomString()))
                .andExpect(status().isNotFound());
    }
}