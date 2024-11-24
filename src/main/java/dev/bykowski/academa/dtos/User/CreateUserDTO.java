package dev.bykowski.academa.dtos.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDTO {

    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    private String givenName;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Confirm password is required")
    private String confirmPassword;

    private String familyName;

    private String picture;

    private String locale;

    @NotNull(message = "Role is required")
    private String role;
}
