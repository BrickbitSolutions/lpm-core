package be.brickbit.lpm.core.config;

import be.brickbit.lpm.core.integration.sms.DefaultSmsTemplateImpl;
import be.brickbit.lpm.core.integration.sms.SMSTemplate;
import be.brickbit.lpm.core.integration.sms.clickatell.ClickatellSmsTemplateImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SMSConfig {
    @Value("${lpm.core.sms.clickatell-token}")
    private String clickatellToken;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SMSTemplate smsTemplate(){
        if(StringUtils.isNotEmpty(clickatellToken)){
            return new ClickatellSmsTemplateImpl(restTemplate(), clickatellToken);
        }

        return new DefaultSmsTemplateImpl();
    }
}
