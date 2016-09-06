package be.brickbit.lpm.core.controller;

import be.brickbit.lpm.core.command.user.UpdateUserProfileCommand;
import be.brickbit.lpm.core.command.user.UpdateUserPasswordCommand;
import be.brickbit.lpm.core.domain.User;
import be.brickbit.lpm.core.fixture.UserDetailsDtoFixture;
import be.brickbit.lpm.core.service.security.SecurityService;
import be.brickbit.lpm.core.service.user.UserService;
import be.brickbit.lpm.core.service.user.dto.AdminUserDetailsDto;
import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.user.mapper.AdminUserDetailsDtoMapper;
import be.brickbit.lpm.core.service.user.mapper.UserDetailsDtoMapper;
import be.brickbit.lpm.core.service.user.mapper.UserPrincipalDtoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@Mock
	private UserService userService;

	@Mock
	private UserPrincipalDtoMapper userPrincipalDtoMapper;

	@Mock
	private UserDetailsDtoMapper userDetailsDtoMapper;

	@Mock
	private AdminUserDetailsDtoMapper adminUserDetailsDtoMapper;

	@Mock
	private SecurityService securityService;

	@InjectMocks
	private UserController controller;

    @Test
    public void findAllAuthorities() throws Exception {
        final ArrayList<String> authorities = new ArrayList<>();
        when(userService.findAllAuthorities()).thenReturn(authorities);

        assertThat(controller.findAllAuthorities()).isSameAs(authorities);
    }

	@Test
	public void getUserPrincipal() throws Exception {
		String username = randomString();

		when(securityService.getAuthenticatedUsername()).thenReturn(username);
		UserPrincipalDto dto = new UserPrincipalDto();
		when(userService.findByUsername(username, userPrincipalDtoMapper)).thenReturn(dto);

		assertThat(controller.getCurrentUserDetails()).isSameAs(dto);
	}

	@Test
	public void getUserDetails() throws Exception {
		Long id = randomLong();
		UserDetailsDto dto = UserDetailsDtoFixture.mutable();
		when(userService.findOne(id, userDetailsDtoMapper)).thenReturn(dto);

		assertThat(controller.getUserDetails(id)).isSameAs(dto);
	}

    @Test
    public void getUserDetailsBySeatNumber() throws Exception {
        Integer seatNumber = randomInt();
        UserDetailsDto dto = UserDetailsDtoFixture.mutable();
        when(userService.findBySeatNumber(seatNumber, userDetailsDtoMapper)).thenReturn(dto);

        assertThat(controller.getUserDetailsBySeat(seatNumber)).isSameAs(dto);
    }

    @Test
    public void getAdminUserDetails() throws Exception {
        Long id = randomLong();
        AdminUserDetailsDto dto = new AdminUserDetailsDto();
        when(userService.findOne(id, adminUserDetailsDtoMapper)).thenReturn(dto);

        assertThat(controller.getAdminUserDetails(id)).isSameAs(dto);
    }

	@Test
	public void getAdminUserOverview() throws Exception {
		final ArrayList<AdminUserDetailsDto> adminUserDetailsDtos = new ArrayList<>();
		when(userService.findAll(adminUserDetailsDtoMapper)).thenReturn(adminUserDetailsDtos);

		assertThat(controller.getAllUsers()).isSameAs(adminUserDetailsDtos);
	}

	@Test
	public void updatesUserProfile() throws Exception {
		User user = new User();
		user.setId(randomLong());
		UpdateUserProfileCommand updateUserProfileCommand = new UpdateUserProfileCommand();

		when(securityService.getAuthenticatedUser()).thenReturn(user);
		controller.updateUserProfile(updateUserProfileCommand);

		verify(userService).updateUserProfile(user.getId(), updateUserProfileCommand);
	}

	@Test
	public void updatesUserPassword() throws Exception {
		User user = new User();
		user.setId(randomLong());
		UpdateUserPasswordCommand updateUserPasswordCommand = new UpdateUserPasswordCommand();

		when(securityService.getAuthenticatedUser()).thenReturn(user);
		controller.updateUserPassword(updateUserPasswordCommand);

		verify(userService).updateUserPassword(user.getId(), updateUserPasswordCommand);
	}
}