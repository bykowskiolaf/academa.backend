package dev.bykowski.academa.repositories;

import dev.bykowski.academa.models.Course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {

    Set<Course> findByInstructor_Uuid(UUID instructorUuid);

}
