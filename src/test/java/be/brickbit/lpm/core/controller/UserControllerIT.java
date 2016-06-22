package be.brickbit.lpm.core.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class UserControllerIT extends AbstractControllerIT {
	@Test
	public void testGetUserPrincipal() throws Exception {
		performGet("/user/principal")
				.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$.mood", isEmptyOrNullString()))
                .andExpect(jsonPath("$.authorities", containsInAnyOrder("ROLE_USER", "ROLE_ADMIN")));
	}

	@Test
	public void testGetUserDetails() throws Exception {
		User user = UserFixture.mutable();
        user.setBirthDate(LocalDate.of(1991, 5, 4));

		insert(user);

		performGet("/user/" + user.getId())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(user.getUsername())))
				.andExpect(jsonPath("$.age", is((int) user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS))))
				.andExpect(jsonPath("$.wallet", is(user.getWallet())))
				.andExpect(jsonPath("$.mood", is(user.getMood())))
				.andExpect(jsonPath("$.firstName", is(user.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(user.getLastName())))
				.andExpect(jsonPath("$.email", is(user.getEmail())));
	}
}