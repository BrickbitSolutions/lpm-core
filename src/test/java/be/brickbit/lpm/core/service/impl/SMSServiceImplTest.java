package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.integration.sms.SMSTemplate;
import be.brickbit.lpm.core.service.api.notify.SMSService;
import be.brickbit.lpm.core.service.impl.internal.api.InternalUserService;
import be.brickbit.lpm.infrastructure.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SMSServiceImplTest {
    @Mock
    private SMSTemplate smsTemplate;

    @Mock
    private InternalUserService userService;

    @InjectMocks
    private SMSServiceImpl smsService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void throwsExceptionOnUserWithoutPhoneNr() throws Exception {
        User user = UserFixture.mutable();
        user.setMobileNr(StringUtils.EMPTY);

        Long userId = randomLong();

        String textMessage = randomString();

        when(userService.findOne(userId)).thenReturn(user);

        expectedException.expect(ServiceException.class);
        expectedException.expectMessage("User has not set a mobile phone nr.");

        smsService.sendSMS(userId, textMessage);
    }

    @Test
    public void sendsTextMessage() throws Exception {
        User user = UserFixture.mutable();
        user.setMobileNr(randomString());

        Long userId = randomLong();

        String textMessage = randomString();

        when(userService.findOne(userId)).thenReturn(user);

        smsService.sendSMS(userId, textMessage);

        verify(smsTemplate, times(1)).sendSMS(
                user.getMobileNr(),
                String.format("Hello %s, %s", user.getUsername(), textMessage)
        );
    }
}