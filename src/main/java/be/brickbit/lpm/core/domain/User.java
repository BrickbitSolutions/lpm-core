package be.brickbit.lpm.core.domain;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "LPM_USER")
public @Data class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "USERNAME")
	private String username;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "FIRSTNAME")
	private String firstName;
	@Column(name = "LASTNAME")
	private String lastName;
	@Column(name = "BIRTHDATE")
	private LocalDate birthDate;
	@Column(name = "EMAIL")
	private String email;
    @Column(name = "SEAT_NUMBER")
    private Integer seatNumber;
	@Column(name = "MOOD")
	private String mood;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "USER_AUTHORITY", joinColumns = {
			@JoinColumn(name = "lpm_user_id", nullable = false),
	}, inverseJoinColumns = {
			@JoinColumn(name = "authority_id", nullable = false)
	})
	private Set<Authority> authorities;
	@Column(name = "NONEXPIRED")
	private boolean accountNonExpired;
	@Column(name = "NONLOCKED")
	private boolean accountNonLocked;
	@Column(name = "CREDSNONEXPIRED")
	private boolean credentialsNonExpired;
	@Column(name = "ENABLED")
	private boolean enabled;

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Set<Authority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
