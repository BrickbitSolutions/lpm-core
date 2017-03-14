package be.brickbit.lpm.core.repository;

import be.brickbit.lpm.core.AbstractRepoIT;
import be.brickbit.lpm.core.domain.ActivationToken;
import be.brickbit.lpm.core.fixture.ActivationTokenFixture;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivationTokenRepositoryTest extends AbstractRepoIT {

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    @Test
    public void findsActivationTokenByToken() throws Exception {
        ActivationToken token = ActivationTokenFixture.mutable();

        insert(
                token.getUser(),
                token
        );

        assertThat(activationTokenRepository.findByToken(token.getToken())).isEqualTo(token);
    }

}