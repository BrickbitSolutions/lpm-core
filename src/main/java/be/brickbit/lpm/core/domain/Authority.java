package be.brickbit.lpm.core.domain;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Entity
@Table(name = "AUTHORITY")
public @Data class Authority implements GrantedAuthority {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	@Column(name = "AUTHORITY")
	private String authority;

	@Override
	public String getAuthority() {
		return authority;
	}
}
