package be.brickbit.lpm.core.service.impl;

import com.google.common.collect.Lists;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import be.brickbit.lpm.core.repository.AuthorityRepository;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorityServiceImplTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @Test
    public void findByAuthority() throws Exception {
        String authName = randomString();
        Optional<Authority> authority = Optional.of(AuthorityFixture.user());

        when(authorityRepository.findByAuthority(authName)).thenReturn(authority);

        Authority result = authorityService.findByAuthority(authName);

        assertThat(result).isSameAs(authority.orElseThrow(Exception::new));
    }

    @Test
    public void findAll() throws Exception {
        List<Authority> authorities = Lists.newArrayList(
                AuthorityFixture.admin(),
                AuthorityFixture.user()
        );

        when(authorityRepository.findAll()).thenReturn(authorities);

        assertThat(authorityService.findAll()).isSameAs(authorities);
    }
}