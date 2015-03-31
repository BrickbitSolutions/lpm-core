package be.brickbit.lpm.core.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
public class Authority implements GrantedAuthority{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String authority;

    public Authority(String authority) {
        this();
        this.authority = authority;
    }

    public Authority() {
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        Authority authority1 = (Authority) o;

        return !(authority != null ? !authority.equals(authority1.authority) : authority1.authority != null);

    }

    @Override
    public int hashCode() {
        return authority != null ? authority.hashCode() : 0;
    }
}
