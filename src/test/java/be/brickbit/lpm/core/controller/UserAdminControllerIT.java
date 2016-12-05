package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.AbstractControllerIT;
import be.brickbit.lpm.core.controller.command.user.AssignSeatCommand;
import be.brickbit.lpm.core.controller.command.user.UpdateAuthoritiesCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserFixture;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.time.format.DateTimeFormatter;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomInt;
import static be.brickbit.lpm.core.util.WiserAssertions.assertReceivedMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAdminControllerIT extends AbstractControllerIT {
    @Test
    public void testFindAllAuthorities() throws Exception {
        performGet("/admin/user/authorities")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", containsInAnyOrder(
                        "ROLE_USER",
                        "ROLE_ADMIN",
                        "ROLE_CATERING_ADMIN",
                        "ROLE_CATERING_CREW"
                )));
    }

    @Test
    public void testGetAdminUserDetails() throws Exception {
        User user = UserFixture.mutable();

        insert(user);

        performGet("/admin/user/" + user.getId() + "/details")
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
    public void assignsNewSeat() throws Exception {
        User user = UserFixture.mutable();
        final Integer newSeatNr = randomInt();

        insert(user);

        performPut("/admin/user/" + user.getId() + "/seat", new AssignSeatCommand(newSeatNr))
                .andExpect(status().isNoContent());

        assertThat(user.getSeatNumber()).isEqualTo(newSeatNr);
    }

    @Test
    public void testGetAllUser() throws Exception {
        User user = UserFixture.testUser();

        performGet("/admin/user")
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

        performPut("/admin/user/" + user.getId() + "/enable", null)
                .andExpect(status().isNoContent());

        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    public void testDisableUser() throws Exception {
        User user = UserFixture.mutable();
        user.setEnabled(true);

        insert(user);

        performPut("/admin/user/" + user.getId() + "/disable", null)
                .andExpect(status().isNoContent());

        assertThat(user.isEnabled()).isFalse();
    }

    @Test
    public void testLockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setAccountNonLocked(true);

        insert(user);

        performPut("/admin/user/" + user.getId() + "/lock", null)
                .andExpect(status().isNoContent());

        assertThat(user.isAccountNonLocked()).isFalse();
    }

    @Test
    public void testUnlockUser() throws Exception {
        User user = UserFixture.mutable();
        user.setAccountNonLocked(false);

        insert(user);

        performPut("/admin/user/" + user.getId() + "/unlock", null)
                .andExpect(status().isNoContent());

        assertThat(user.isAccountNonLocked()).isTrue();
    }

    @Test
    public void updateUserAccountDetails() throws Exception {
        User user = UserFixture.mutable();
        UpdateAuthoritiesCommand command = new UpdateAuthoritiesCommand(
                Lists.newArrayList("ROLE_USER", "ROLE_ADMIN")
        );

        insert(
                user
        );

        performPut("/admin/user/" + user.getId() + "/authorities", command)
                .andExpect(status().isNoContent());

        assertThat(user.getAuthorities()).hasSize(2);
    }

    @Test
    public void resetsPassword() throws Exception {
        User user = UserFixture.mutable();

        insert(user);

        performPut("/admin/user/" + user.getId() + "/password", null)
                .andExpect(status().isNoContent());

        assertReceivedMessage(wiser())
                .from("me@localhost")
                .to(user.getEmail())
                .withSubject("[LPM] Password Reset");
    }
}