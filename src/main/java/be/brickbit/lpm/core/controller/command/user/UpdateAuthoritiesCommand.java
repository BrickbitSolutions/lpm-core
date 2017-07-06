package be.brickbit.lpm.core.controller.command.user;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAuthoritiesCommand {
    @NotEmpty(message = "User must have at least 1 authority")
    private List<String> authorities;
}
