package be.brickbit.lpm.core.integration.sms.clickatell;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
class ClickatellMessage {
    private Boolean accepted;
    private String to;
    private String apiMessageId;
}
