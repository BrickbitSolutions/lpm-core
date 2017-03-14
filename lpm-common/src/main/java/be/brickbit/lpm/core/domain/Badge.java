package be.brickbit.lpm.core.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "USER_BADGE")
@Data
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LPM_USER_ID")
    private User user;

    @Column(name = "TOKEN", unique = true)
    private String token;

    @Column(name = "ENABLED")
    private Boolean enabled;
}
