package be.brickbit.lpm.sms.clickatell;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import be.brickbit.lpm.sms.SMSTemplate;

public class ClickatellSmsTemplateImpl implements SMSTemplate {
    private RestTemplate restTemplate;
    private String clickatellToken;

    @Autowired
    public ClickatellSmsTemplateImpl(RestTemplate restTemplate, String clickatellToken) {
        this.restTemplate = restTemplate;
        this.clickatellToken = clickatellToken;
    }

    @Override
    public void sendSMS(String phoneNr, String text) {
        ClickatellRequest clickatellRequest = new ClickatellRequest(
                text,
                Lists.newArrayList(phoneNr)
        );

        HttpEntity<ClickatellRequest> requestHttpEntity = new HttpEntity<>(clickatellRequest, getClickatellHeaders());

        ResponseEntity<ClickatellResponse> response =
                restTemplate.exchange(
                        "https://api.clickatell.com/rest/message",
                        HttpMethod.POST,
                        requestHttpEntity,
                        ClickatellResponse.class
                );

        ClickatellMessage responseMessage = response.getBody().getData().getMessage().get(0);

        if (!responseMessage.getAccepted()) {
            throw new RuntimeException(String.format("SMS Failed to send to: %s. Clickatell ID: %s", responseMessage.getTo(), responseMessage.getApiMessageId()));
        }
    }

    private HttpHeaders getClickatellHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Version", "1");
        headers.add("Authorization", "bearer " + clickatellToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        return headers;
    }
}
