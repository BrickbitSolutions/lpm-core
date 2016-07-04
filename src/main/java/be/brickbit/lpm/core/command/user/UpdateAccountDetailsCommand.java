package be.brickbit.lpm.core.command.user;

import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDetailsCommand {
	@NotBlank(message = "Username may not be empty")
	private String username;
	@Email
	@NotBlank(message = "Email may not be empty")
	private String email;
	@NotEmpty(message = "User must have at least 1 authority")
	private List<String> authorities;
}
