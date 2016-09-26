package be.brickbit.lpm.core.service.impl;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.fixture.AuthorityFixture;
import be.brickbit.lpm.core.repository.AuthorityRepository;
import be.brickbit.lpm.core.service.api.authority.AuthorityDtoMapper;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static be.brickbit.lpm.core.util.RandomValueUtil.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorityServiceImplTest {

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private AuthorityDtoMapper mapper;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    @Test
    public void findAll() throws Exception {
        final Authority admin = AuthorityFixture.admin();
        String authorityName = randomString();
        List<Authority> authorities = Lists.newArrayList(
                admin
        );

        when(authorityRepository.findAll()).thenReturn(authorities);
        when(mapper.map(admin)).thenReturn(authorityName);

        assertThat(authorityService.findAll(mapper)).containsExactly(authorityName);
    }
}