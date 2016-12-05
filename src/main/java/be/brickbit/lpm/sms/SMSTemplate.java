package be.brickbit.lpm.sms;

public interface SMSTemplate {
    void sendSMS(String phoneNr, String text);
}
