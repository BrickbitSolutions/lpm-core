package be.brickbit.lpm.sms;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import be.brickbit.lpm.sms.exception.NoSMSServiceException;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;

public class DefaultSmsTemplateImplTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private SMSTemplate smsService;

    @Before
    public void setUp() throws Exception {
        smsService = new DefaultSmsTemplateImpl();
    }

    @Test
    public void throwsNoServiceConfiguredException() throws Exception {
        expectedException.expect(NoSMSServiceException.class);
        expectedException.expectMessage("There is no SMS Service configured");

        smsService.sendSMS(randomString(), randomString());
    }
}