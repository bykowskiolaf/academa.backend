package dev.bykowski.academa.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID uuid;
    private String email;
    private String name;
    private String role;
}
