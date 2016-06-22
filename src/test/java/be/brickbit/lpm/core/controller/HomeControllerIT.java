package be.brickbit.lpm.core.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.fixture.command.NewUserCommandFixture;

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