package be.brickbit.lpm.core.command.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileCommand {
    @NotBlank(message = "Username may not be empty")
    private String username;
    @Email
    @NotBlank(message = "Email may not be empty")
    private String email;
    private String mood;
}
