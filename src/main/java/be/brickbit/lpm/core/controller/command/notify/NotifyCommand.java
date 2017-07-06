package be.brickbit.lpm.core.controller.command.notify;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotifyCommand {
    @NotNull(message = "user ID is mandatory.")
    private Long userId;
    @NotBlank(message = "message can not be empty.")
    private String message;
}
