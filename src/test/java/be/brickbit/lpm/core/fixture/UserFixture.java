package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.domain.Authority;
import be.brickbit.lpm.core.domain.User;

import java.util.Collections;
import java.util.Set;

public class UserFixture {
    public static User getUser(){
        return new User("soulscammer", "password", "Jonas", "Liekens", "soulscammer@gmail.com", getAuthorities());
    }

    private static Set<Authority> getAuthorities(){
        return Collections.singleton(new Authority("ROLE_USER"));
    }
}
