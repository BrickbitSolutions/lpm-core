package be.brickbit.lpm.core.controller.command;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserEmailCommand {
    @Email(message = "email is not valid")
    @NotBlank(message = "email cannot be blank.")
    private String email;
}
