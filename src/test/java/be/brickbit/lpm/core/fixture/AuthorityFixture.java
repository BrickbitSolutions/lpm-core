package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.domain.Authority;

public class AuthorityFixture {
    public static Authority user(){
        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        return authority;
    }

    public static Authority admin(){
        Authority authority = new Authority();
        authority.setAuthority("ROLE_ADMIN");
        return authority;
    }
}
