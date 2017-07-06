package be.brickbit.lpm.sms.smsgatewayme;

import org.springframework.web.client.RestTemplate;

import be.brickbit.lpm.sms.SMSTemplate;

public class SmsGatewayTemplateImpl implements SMSTemplate {
    private RestTemplate restTemplate;
    private String email;
    private String password;
    private String deviceId;

    public SmsGatewayTemplateImpl(RestTemplate restTemplate, String email, String password, String deviceId) {
        this.restTemplate = restTemplate;
        this.email = email;
        this.password = password;
        this.deviceId = deviceId;
    }

    @Override
    public void sendSMS(String phoneNr, String text) {
        SmsGatewayRequest request = new SmsGatewayRequest(
                email,
                password,
                deviceId,
                phoneNr,
                text
        );

        restTemplate.postForObject(
                "https://smsgateway.me/api/v3/messages/send",
                request,
                String.class
        );
    }
}
