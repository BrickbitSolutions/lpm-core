package be.brickbit.lpm.sms.clickatell;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
class ClickatellResponse {
    private ClickatellData data;
}