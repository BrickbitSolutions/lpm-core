package be.brickbit.lpm.core.controller;

import com.mysema.query.jpa.impl.JPAQuery;

import org.junit.Test;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.domain.ActivationToken;
import be.brickbit.lpm.core.domain.QActivationToken;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.ActivationTokenFixture;
import be.brickbit.lpm.core.fixture.UserFixture;
import be.brickbit.lpm.core.fixture.command.NewUserCommandFixture;

import static be.brickbit.lpm.core.util.WiserAssertions.assertReceivedMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeControllerIT extends AbstractControllerIT {
    @Test
    public void getHello() throws Exception {
        performGet("/")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("UP")));
    }

    @Test
    public void getRegister() throws Exception {
        performPost("/register", NewUserCommandFixture.mutable())
                .andExpect(status().isCreated());

        assertReceivedMessage(wiser())
                .from("me@localhost")
                .withSubject("[LPM] Account Activation");
    }

    @Test
    public void activatesUser() throws Exception {
        User user = UserFixture.mutable();
        user.setEnabled(false);
        ActivationToken token = ActivationTokenFixture.mutable();
        token.setUser(user);

        insert(
                user,
                token
        );

        performPut("/activate/" + token.getToken(), null)
                .andExpect(status().isOk());

        assertThat(user.isEnabled()).isTrue();

        ActivationToken result = new JPAQuery(getEntityManager())
                .from(QActivationToken.activationToken)
                .where(QActivationToken.activationToken.token.eq(token.getToken()))
                .uniqueResult(QActivationToken.activationToken);

        assertThat(result).isNull();
    }
}