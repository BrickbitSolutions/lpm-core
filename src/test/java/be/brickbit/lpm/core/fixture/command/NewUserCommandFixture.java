package be.brickbit.lpm.core.fixture.command;

import be.brickbit.lpm.core.command.home.NewUserCommand;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomEmail;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomLocalDate;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;

public class NewUserCommandFixture {
    public static NewUserCommand mutable(){
        return new NewUserCommand()
                .setEmail(randomEmail())
                .setBirthDate(randomLocalDate())
                .setFirstName(randomString())
                .setLastName(randomString())
                .setPassword(randomString())
                .setUsername(randomString());
    }
}
