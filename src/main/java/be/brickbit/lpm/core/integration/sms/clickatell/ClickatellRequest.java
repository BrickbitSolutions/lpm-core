package be.brickbit.lpm.core.integration.sms.clickatell;

import lombok.Value;

import java.util.List;

@Value
class ClickatellRequest {
    private String text;
    private List<String> to;
}
