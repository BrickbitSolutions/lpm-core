package be.brickbit.lpm.core.command.home;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class NewUserCommand {
    @NotBlank(message = "Username may not be empty")
    private String username;
    @NotBlank(message = "Password may not be empty")
    private String password;
    @NotBlank(message = "First Name may not be empty")
    private String firstName;
    @NotBlank(message = "Last Name may not be empty")
    private String lastName;
    @Email
    @NotBlank(message = "Email may not be empty")
    private String email;
    @NotNull(message = "Birthdate may not be empty")
    private LocalDate birthDate;

    public String getUsername() {
        return username;
    }

    public NewUserCommand setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public NewUserCommand setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public NewUserCommand setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public NewUserCommand setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public NewUserCommand setEmail(String email) {
        this.email = email;
        return this;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public NewUserCommand setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }
}
