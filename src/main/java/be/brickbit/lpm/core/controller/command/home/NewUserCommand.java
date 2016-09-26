package be.brickbit.lpm.core.controller.command.home;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserCommand {
    @NotBlank(message = "Username may not be empty")
    private String username;
    @NotBlank(message = "Password may not be empty")
    private String password;
    @NotBlank(message = "First Name may not be empty")
    private String firstName;
    @NotBlank(message = "Last Name may not be empty")
    private String lastName;
    @Email
    @NotBlank(message = "Email may not be empty")
    private String email;
    @NotNull(message = "Birthdate may not be empty")
    private LocalDate birthDate;
}
