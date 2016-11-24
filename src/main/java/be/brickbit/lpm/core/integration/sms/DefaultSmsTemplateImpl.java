package be.brickbit.lpm.core.integration.sms;


import be.brickbit.lpm.core.integration.exception.NoSMSServiceException;

public class DefaultSmsTemplateImpl implements SMSTemplate {
    @Override
    public void sendSMS(String phoneNr, String text) {
        throw new NoSMSServiceException("There is no SMS Service configured");
    }
}
