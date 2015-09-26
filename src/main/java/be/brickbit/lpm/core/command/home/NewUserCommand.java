package be.brickbit.lpm.core.command.home;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

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
}
