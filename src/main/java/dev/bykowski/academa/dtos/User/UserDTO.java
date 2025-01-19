package dev.bykowski.academa.dtos.User;

import dev.bykowski.academa.models.User.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID uuid;

    private String email;

    private String givenName;

    private String familyName;

    private String picture;

    private String locale;

    private String role;

    public UserDTO(User user) {
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        this.givenName = user.getGivenName();
        this.familyName = user.getFamilyName();
        this.picture = user.getPicture();
        this.locale = user.getLocale();
        this.role = user.getRole().getName();
    }
}
