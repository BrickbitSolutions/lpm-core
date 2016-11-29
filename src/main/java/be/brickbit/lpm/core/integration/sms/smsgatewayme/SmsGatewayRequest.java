package be.brickbit.lpm.core.integration.sms.smsgatewayme;

import lombok.Value;

@Value
public class SmsGatewayRequest {
    private String email;
    private String password;
    private String device;
    private String number;
    private String message;
}
