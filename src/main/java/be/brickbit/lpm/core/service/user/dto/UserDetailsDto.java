package be.brickbit.lpm.core.service.user.dto;

import java.math.BigDecimal;

public class UserDetailsDto {
    private String username;
    private Long age;
    private BigDecimal wallet;
    private String mood;
    private String firstName;
    private String lastName;
    private String email;

    public UserDetailsDto(String username, Long age, BigDecimal wallet, String mood, String firstName, String lastName, String
            email) {
        this.username = username;
        this.age = age;
        this.wallet = wallet;
        this.mood = mood;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public Long getAge() {
        return age;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public String getMood() {
        return mood;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
