package be.brickbit.lpm.core.fixture.command;

import be.brickbit.lpm.core.controller.command.home.NewUserCommand;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomEmail;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomLocalDate;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;

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
