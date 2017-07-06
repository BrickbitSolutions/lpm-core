package be.brickbit.lpm.sms.config;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import be.brickbit.lpm.sms.DefaultSmsTemplateImpl;
import be.brickbit.lpm.sms.SMSTemplate;
import be.brickbit.lpm.sms.clickatell.ClickatellSmsTemplateImpl;
import be.brickbit.lpm.sms.smsgatewayme.SmsGatewayTemplateImpl;

@Configuration
public class SMSConfig {
    @Value("${lpm.sms.clickatell-token:}")
    private String clickatellToken;

    @Value("${lpm.sms.smsgateway.email:}")
    private String smsGatewayEmail;

    @Value("${lpm.sms.smsgateway.password:}")
    private String smsGatewayPassword;

    @Value("${lpm.sms.smsgateway.deviceId:}")
    private String smsGatewayDeviceId;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SMSTemplate smsTemplate() {
        if (StringUtils.isNotEmpty(clickatellToken)) {
            return new ClickatellSmsTemplateImpl(restTemplate(), clickatellToken);
        }

        if (StringUtils.isNotEmpty(smsGatewayEmail) &&
                StringUtils.isNotEmpty(smsGatewayPassword) &&
                StringUtils.isNotEmpty(smsGatewayDeviceId)) {
            return new SmsGatewayTemplateImpl(restTemplate(), smsGatewayEmail, smsGatewayPassword, smsGatewayDeviceId);
        }

        return new DefaultSmsTemplateImpl();
    }
}
