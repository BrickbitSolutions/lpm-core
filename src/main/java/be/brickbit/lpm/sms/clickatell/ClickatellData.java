package be.brickbit.lpm.sms.clickatell;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
class ClickatellData {
    private List<ClickatellMessage> message;
}