package be.brickbit.lpm.core.controller.command.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssignSeatCommand {
    @NotNull(message = "Seat number cannot be null")
    @Min(value = 1, message = "Seat number cannot be zero or negative")
    private Integer seatNumber;
}
