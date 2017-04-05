package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.controller.command.UpdateUserEmailCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAuthoritiesCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomEmail;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static be.brickbit.lpm.core.util.WiserAssertions.assertReceivedMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends AbstractControllerIT {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testGetUserPrincipal() throws Exception {
        User user = UserFixture.testUser();

        performGet("/user")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.mood", is(user.getMood())))
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
                .andExpect(jsonPath("$.mood", is(user.getMood())))
                .andExpect(jsonPath("$.seatNumber", is(user.getSeatNumber())));
    }

    @Test
    public void getsUserProfile() throws Exception {
        User user = UserFixture.testUser();

        performGet("/user/profile")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.seatNumber", is(user.getSeatNumber())))
                .andExpect(jsonPath("$.mood", is(user.getMood())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.birthDate", is(user.getBirthDate().format(DateTimeFormatter.ISO_DATE))))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.mobilePhoneNr", is(user.getMobileNr())));
    }

    @Test
    public void testGetUserDetailsBySeat() throws Exception {
        User user = UserFixture.mutable();
        user.setBirthDate(LocalDate.of(1991, 5, 4));

        insert(user);

        performGet("/user/seat/" + user.getSeatNumber())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.age", is((int) user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS))))
                .andExpect(jsonPath("$.mood", is(user.getMood())))
                .andExpect(jsonPath("$.seatNumber", is(user.getSeatNumber())));
    }

    @Test
    public void updatesUserProfile() throws Exception {
        User user = getEntityManager().find(User.class, 1L);
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                randomString(),
                randomString()
        );

        performPut("/user/profile", command)
                .andExpect(status().isNoContent());

        assertThat(user.getMobileNr()).isEqualTo(command.getMobileNr());
        assertThat(user.getMood()).isEqualTo(command.getMood());
    }

    @Test
    public void updateUserPassword() throws Exception {
        User user = getEntityManager().find(User.class, 1L);
        UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(randomString());

        performPut("/user/password", command)
                .andExpect(status().isNoContent());

        assertThat(passwordEncoder.matches(command.getPassword(), user.getPassword())).isTrue();
    }

    @Test
    public void updateUserEmail() throws Exception {
        User user = getEntityManager().find(User.class, 1L);
        UpdateUserEmailCommand command = new UpdateUserEmailCommand(randomEmail());

        performPut("/user/email", command)
                .andExpect(status().isNoContent());

        assertThat(command.getEmail()).isEqualTo(user.getEmail());
    }
}