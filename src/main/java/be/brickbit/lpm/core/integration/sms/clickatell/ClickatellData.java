package be.brickbit.lpm.core.integration.sms.clickatell;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
class ClickatellData {
    private List<ClickatellMessage> message;
}