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
    @NotBlank(message = "Matching password may not be empty")
    private String firstName;
    @NotBlank(message = "Last Name may not be empty")
    private String lastName;
    @Email
    @NotBlank(message = "Email may not be empty")
    private String email;
    @NotNull(message = "Birthdate may not be empty")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
