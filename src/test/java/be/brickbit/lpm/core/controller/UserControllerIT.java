package be.brickbit.lpm.core.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class UserControllerIT extends AbstractControllerIT {
	@Test
	public void testGetUserPrincipal() throws Exception {
		performGet("/user/principal")
				.andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$.mood", is("Hello LPM")))
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

    @Test
    public void testGetAllUser() throws Exception {
        User user = UserFixture.testUser();

        performGet("/user")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[0].firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(user.getLastName())))
                .andExpect(jsonPath("$[0].email", is(user.getEmail())))
                .andExpect(jsonPath("$[0].birthDate", is(user.getBirthDate().format(DateTimeFormatter.ISO_DATE))))
                .andExpect(jsonPath("$[0].enabled", is(user.isEnabled())))
                .andExpect(jsonPath("$[0].locked", is(!user.isAccountNonLocked())))
                .andExpect(jsonPath("$[0].authorities", containsInAnyOrder("ROLE_USER", "ROLE_ADMIN")));
    }

    @Test
    public void testEnableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setEnabled(false);

        insert(user);

        performPut("/user/" + user.getId() + "/enable", null)
                .andExpect(status().isNoContent());

        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testDisableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setEnabled(true);

        insert(user);

        performPut("/user/" + user.getId() + "/disable", null)
                .andExpect(status().isNoContent());

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testLockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setAccountNonLocked(true);

        insert(user);

        performPut("/user/" + user.getId() + "/lock", null)
                .andExpect(status().isNoContent());

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void testUnlockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setAccountNonLocked(false);

        insert(user);

        performPut("/user/" + user.getId() + "/unlock", null)
                .andExpect(status().isNoContent());

        assertThat(user.isAccountNonLocked()).isTrue();
    }
}