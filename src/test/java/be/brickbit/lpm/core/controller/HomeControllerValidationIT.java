package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.controller.command.home.NewUserCommand;
import org.junit.Test;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeControllerValidationIT extends AbstractControllerIT {

    @Test
    public void registerNewUser__Invalid() throws Exception {
        performPost("/register", new NewUserCommand())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.cause", is("Validation of object failed")))
                .andExpect(jsonPath("$.validationMessages", hasEntry("username", "Username may not be empty")))
                .andExpect(jsonPath("$.validationMessages", hasEntry("password", "Password may not be empty")))
                .andExpect(jsonPath("$.validationMessages", hasEntry("firstName", "First Name may not be empty")))
                .andExpect(jsonPath("$.validationMessages", hasEntry("lastName", "Last Name may not be empty")))
                .andExpect(jsonPath("$.validationMessages", hasEntry("email", "Email may not be empty")))
                .andExpect(jsonPath("$.validationMessages", hasEntry("birthDate", "Birthdate may not be empty")));
    }
}