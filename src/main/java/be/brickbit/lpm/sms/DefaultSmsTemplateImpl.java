package be.brickbit.lpm.sms;


import be.brickbit.lpm.sms.exception.NoSMSServiceException;

public class DefaultSmsTemplateImpl implements SMSTemplate {
    @Override
    public void sendSMS(String phoneNr, String text) {
        throw new NoSMSServiceException("There is no SMS Service configured");
    }
}
