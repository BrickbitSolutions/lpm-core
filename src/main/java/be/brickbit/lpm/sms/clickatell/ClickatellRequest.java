package be.brickbit.lpm.sms.clickatell;

import java.util.List;

import lombok.Value;

@Value
class ClickatellRequest {
    private String text;
    private List<String> to;
}
