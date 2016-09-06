package be.brickbit.lpm.core.fixture;

import be.brickbit.lpm.core.domain.User;
import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.util.HashSet;

import static be.brickbit.lpm.core.util.RandomValueUtil.*;

public class UserFixture {
	public static User mutable() {
		User user = new User();

        user.setSeatNumber(randomInt());
		user.setEmail(randomEmail());
		user.setUsername(randomString());
		user.setPassword(randomString());
		user.setFirstName(randomString());
		user.setLastName(randomString());
		user.setBirthDate(randomLocalDate());
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(true);
		user.setMood(randomString(50));
		user.setWallet(randomDecimal());
		user.setAuthorities(new HashSet<>());

		return user;
	}

    public static User testUser() {
        User user = new User();

        user.setEmail("mail@mail.be");
        user.setUsername("admin");
        user.setPassword("$2a$10$asqus3jJEvovoibX5vyhGe.kk65OMkKRv19bzlKLczxJM7Xs0dBY6");
        user.setFirstName("Default");
        user.setLastName("Admin");
        user.setBirthDate(LocalDate.of(1991, 5, 4));
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        user.setMood(randomString(50));
        user.setWallet(randomDecimal());
        user.setAuthorities(Sets.newHashSet(AuthorityFixture.admin(), AuthorityFixture.user()));

        return user;
    }
}
