package be.brickbit.lpm.core.controller.command.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfileCommand {
    private String mood;
    private String mobileNr;
}
