package be.brickbit.lpm.core.service.api.notify;

public interface SMSService {
    void sendSMS(Long userId, String text);
}
