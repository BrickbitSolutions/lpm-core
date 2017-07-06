package be.brickbit.lpm.core.controller.command.user;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPasswordCommand {
    @NotBlank(message = "Password may not be empty")
    private String password;
}
