package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Course.CreateCourseDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.Course.Course;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.repositories.CourseRepository;
import dev.bykowski.academa.repositories.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseService {
    private CourseRepository courseRepository;

    private StudentRepository studentRepository;

    private UserService userService;

    public List<CourseDTO> getAll() {
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getByUuid(UUID uuid) {
        return courseRepository.findById(uuid)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Boolean existsByUuid(UUID uuid) {
        return courseRepository.existsById(uuid);
    }

    public CourseDTO createCourse(CreateCourseDTO createCourseDTO, UUID instructorUuid) {
        System.out.println(createCourseDTO);

        User instructor = userService.getByUuid(instructorUuid);

        Course course = Course.builder()
                .name(createCourseDTO.getName())
                .description(createCourseDTO.getDescription())
                .instructor(instructor)
                .build();

        System.out.println(course);
        return mapToDTO(courseRepository.save(course));
    }

    public CourseDTO updateCourse(UUID uuid, CreateCourseDTO createCourseDTO) {
        Course course = courseRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(createCourseDTO.getName());
        course.setDescription(createCourseDTO.getDescription());
        return mapToDTO(courseRepository.save(course));
    }

    public List<CourseDTO> assignCourse(UUID studentUuid, UUID courseUuid) {
        Student student = studentRepository.findById(studentUuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", studentUuid)
        ));
        Course course = courseRepository.findById(courseUuid).orElseThrow(() -> new NotFoundException(
                String.format("Course with uuid %s not found", courseUuid)
        ));

        student.getCourses().add(course);

        course.getStudents().add(student);

        studentRepository.save(student);

        return student.getCourses().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public void unassignCourse(UUID studentUuid, UUID courseUuid) {
        Student student = studentRepository.findById(studentUuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", studentUuid)
        ));
        Course course = courseRepository.findById(courseUuid).orElseThrow(() -> new NotFoundException(
                String.format("Course with uuid %s not found", courseUuid)
        ));

        student.getCourses().remove(course);

        course.getStudents().remove(student);

        studentRepository.save(student);
    }

    public List<CourseDTO> getStudentCourses(UUID studentUuid) {
        Student student = studentRepository.findById(studentUuid)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Student with uuid %s not found", studentUuid)
                ));
        return student.getCourses().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public Boolean isOwner(UUID courseUuid, UUID instructorUuid) {

        Course course = courseRepository.findById(courseUuid)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Course with uuid %s not found", courseUuid)
                ));

        return course.getInstructor().getUuid().equals(instructorUuid);
    }

    public Boolean isOwnerOrAdmin(UUID courseUuid, UUID instructorUuid) {
        return isOwner(courseUuid, instructorUuid) || userService.isAdmin(instructorUuid);
    }

    public void deleteCourse(UUID uuid) {
        courseRepository.deleteById(uuid);
    }

    public CourseDTO mapToDTO(Course course) {
        return new CourseDTO(course);
    }
}
