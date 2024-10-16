package dev.bykowski.academa.models;

import dev.bykowski.academa.models.User.User;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Student extends User {
        private String studentId;
        private String studentClass;
}
