package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.fixture.command.NewUserCommandFixture;
import org.junit.Test;

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
    }
}