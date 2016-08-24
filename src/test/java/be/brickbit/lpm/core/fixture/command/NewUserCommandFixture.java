package be.brickbit.lpm.core.fixture.command;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;

import be.brickbit.lpm.core.command.home.NewUserCommand;

public class NewUserCommandFixture {
	public static NewUserCommand mutable() {
		NewUserCommand command = new NewUserCommand();
        
		command.setEmail(randomEmail());
		command.setBirthDate(randomLocalDate());
		command.setFirstName(randomString());
		command.setLastName(randomString());
		command.setPassword(randomString());
		command.setUsername(randomString());

        return command;
	}
}
