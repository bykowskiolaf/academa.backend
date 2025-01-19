package dev.bykowski.academa.models.Course;

import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private UUID uuid = UUID.randomUUID();

    @Size(max = 32, message = "Course name cannot be longer than 32 characters")
    @NotNull(message = "Course name cannot be null")
    private String name;

    @Size(max = 64, message = "Course short description cannot be longer than 255 characters")
    private String shortDescription;

    @Size(max = 2048, message = "Course long description cannot be longer than 512 characters")
    private String longDescription;

    private String picture;

//    @NotNull(message = "Course date cannot be null")
//    private Date date;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private User instructor;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(mappedBy = "enrolledCourses", fetch = FetchType.EAGER)
    private Set<Student> students;

}
