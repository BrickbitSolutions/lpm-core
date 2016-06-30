package be.brickbit.lpm.core.controller;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomLong;
import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import be.brickbit.lpm.core.fixture.UserDetailsDtoFixture;
import be.brickbit.lpm.core.service.security.SecurityService;
import be.brickbit.lpm.core.service.user.UserService;
import be.brickbit.lpm.core.service.user.dto.AdminUserDetailsDto;
import be.brickbit.lpm.core.service.user.dto.UserDetailsDto;
import be.brickbit.lpm.core.service.user.dto.UserPrincipalDto;
import be.brickbit.lpm.core.service.user.mapper.AdminUserDetailsDtoMapper;
import be.brickbit.lpm.core.service.user.mapper.UserDetailsDtoMapper;
import be.brickbit.lpm.core.service.user.mapper.UserPrincipalDtoMapper;

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
	public void getUserPrincipal() throws Exception {
		String username = randomString();

		when(securityService.getAuthenticatedUsername()).thenReturn(username);
		UserPrincipalDto dto = new UserPrincipalDto();
		when(userService.findByUsername(username, userPrincipalDtoMapper)).thenReturn(dto);

		assertThat(controller.getUserPrincipal()).isSameAs(dto);
	}

	@Test
	public void getUserDetails() throws Exception {
		Long id = randomLong();
		UserDetailsDto dto = UserDetailsDtoFixture.mutable();
		when(userService.findOne(id, userDetailsDtoMapper)).thenReturn(dto);

		assertThat(controller.getUserDetails(id)).isSameAs(dto);
	}

	@Test
	public void getAdminUserOverview() throws Exception {
        final ArrayList<AdminUserDetailsDto> adminUserDetailsDtos = new ArrayList<>();
        when(userService.findAll(adminUserDetailsDtoMapper)).thenReturn(adminUserDetailsDtos);

        assertThat(controller.getAllUsers()).isSameAs(adminUserDetailsDtos);
	}
}