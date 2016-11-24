package be.brickbit.lpm.core.integration.sms;

public interface SMSTemplate {
    void sendSMS(String phoneNr, String text);
}
