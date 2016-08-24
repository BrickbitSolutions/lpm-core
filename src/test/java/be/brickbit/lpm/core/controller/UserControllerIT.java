package be.brickbit.lpm.core.controller;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomEmail;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import com.google.common.collect.Lists;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.command.user.UpdateAccountDetailsCommand;
import be.brickbit.lpm.core.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;

public class UserControllerIT extends AbstractControllerIT {
    @Test
    public void testFindAllAuthorities() throws Exception {
        performGet("/user/authorities")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsInAnyOrder(
                        "ROLE_USER",
                        "ROLE_ADMIN",
                        "ROLE_CATERING_ADMIN",
                        "ROLE_CATERING_CREW"
                )));
    }

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
    public void testGetUserDetailsBySeat() throws Exception {
        User user = UserFixture.mutable();
        user.setBirthDate(LocalDate.of(1991, 5, 4));

        insert(user);

        performGet("/user/seat/" + user.getSeatNumber())
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
    public void testGetAdminUserDetails() throws Exception {
        User user = UserFixture.mutable();

        insert(user);

        performGet("/user/" + user.getId() + "/details")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(user.getLastName())))
                .andExpect(jsonPath("$.email", is(user.getEmail())))
                .andExpect(jsonPath("$.birthDate", is(user.getBirthDate().format(DateTimeFormatter.ISO_DATE))))
                .andExpect(jsonPath("$.enabled", is(user.isEnabled())))
                .andExpect(jsonPath("$.locked", is(!user.isAccountNonLocked())))
                .andExpect(jsonPath("$.authorities", notNullValue()));
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

    @Test
    public void updateUserAccountDetails() throws Exception {
        User user = UserFixture.mutable();
        UpdateAccountDetailsCommand command = new UpdateAccountDetailsCommand(
                randomString(),
                randomEmail(),
                Lists.newArrayList("ROLE_USER", "ROLE_ADMIN")
        );

        insert(
                user
        );

        performPut("/user/" + user.getId(), command)
                .andExpect(status().isNoContent());

        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getAuthorities()).hasSize(2);
    }

    @Test
    public void updateUserProfile() throws Exception {
        User user = getEntityManager().find(User.class, 1L);
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                randomString(),
                randomEmail(),
                randomString()
        );

        performPut("/user/profile", command)
                .andExpect(status().isNoContent());

        assertThat(user.getUsername()).isEqualTo(command.getUsername());
        assertThat(user.getEmail()).isEqualTo(command.getEmail());
        assertThat(user.getMood()).isEqualTo(command.getMood());
    }
}