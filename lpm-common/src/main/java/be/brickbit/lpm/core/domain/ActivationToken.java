package be.brickbit.lpm.core.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "ACTIVATION_TOKEN")
@Data
public class ActivationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "LPM_USER_ID")
    private User user;
    @Column(name = "TOKEN")
    private String token;
}
