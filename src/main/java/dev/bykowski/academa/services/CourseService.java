package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Course.CreateCourseDTO;
import dev.bykowski.academa.dtos.Course.FullCourseDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.Course.Course;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.repositories.CourseRepository;
import dev.bykowski.academa.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Page<CourseDTO> getPaginatedCourses(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return courseRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public FullCourseDTO getByUuid(UUID uuid) {
        return courseRepository.findById(uuid)
                .map(FullCourseDTO::new)
                .orElseThrow(() -> new NotFoundException("Course not found"));
    }

    public Boolean existsByUuid(UUID uuid) {
        return courseRepository.existsById(uuid);
    }

    public CourseDTO createCourse(CreateCourseDTO createCourseDTO, UUID instructorUuid) {
        System.out.println(createCourseDTO);

        User instructor = userService.getByUuid(instructorUuid);

        Course course = Course.builder()
                .name(createCourseDTO.getName())
                .shortDescription(createCourseDTO.getShortDescription())
                .longDescription(createCourseDTO.getLongDescription())
                .instructor(instructor)
                .picture(createCourseDTO.getPicture())
                .build();

        System.out.println(course);
        return mapToDTO(courseRepository.save(course));
    }

    public CourseDTO updateCourse(UUID uuid, CreateCourseDTO createCourseDTO) {
        Course course = courseRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        course.setName(createCourseDTO.getName());
        course.setShortDescription(createCourseDTO.getShortDescription());
        course.setLongDescription(createCourseDTO.getLongDescription());
        return mapToDTO(courseRepository.save(course));
    }

    @Transactional
    public List<CourseDTO> assignCourse(UUID studentUuid, UUID courseUuid) {
        Student student = studentRepository.findById(studentUuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", studentUuid)
        ));
        Course course = courseRepository.findById(courseUuid).orElseThrow(() -> new NotFoundException(
                String.format("Course with uuid %s not found", courseUuid)
        ));

        student.getEnrolledCourses().add(course);

        course.getStudents().add(student);

        studentRepository.save(student);

        return student.getEnrolledCourses().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unassignCourse(UUID studentUuid, UUID courseUuid) {
        Student student = studentRepository.findById(studentUuid).orElseThrow(() -> new NotFoundException(
                String.format("Student with uuid %s not found", studentUuid)
        ));
        Course course = courseRepository.findById(courseUuid).orElseThrow(() -> new NotFoundException(
                String.format("Course with uuid %s not found", courseUuid)
        ));

        student.getEnrolledCourses().remove(course);

        course.getStudents().remove(student);

        studentRepository.save(student);
    }

    @Transactional
    public List<CourseDTO> getStudentCourses(UUID studentUuid) {
        Student student = studentRepository.findById(studentUuid)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Student with uuid %s not found", studentUuid)
                ));
        return student.getEnrolledCourses().stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }


    public void deleteCourse(UUID uuid) {
        if (!existsByUuid(uuid)) {
            throw new NotFoundException("Course with given uuid does not exist");
        }
        courseRepository.deleteById(uuid);
    }

    public CourseDTO mapToDTO(Course course) {
        return new CourseDTO(course);
    }

    public List<CourseDTO> getInstructorCourses(UUID uuid) {
        return courseRepository.findByInstructor_Uuid(uuid).stream()
                .map(CourseDTO::new)
                .collect(Collectors.toList());
    }

    public List<StudentDTO> getCourseStudents(UUID uuid) {
        Course course = courseRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Course with uuid %s not found", uuid)
                ));

        return course.getStudents().stream()
                .map(StudentDTO::new)
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

    public Boolean isLoggedInUserOwnerOrAdmin(UUID courseUuid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getByEmail(auth.getName());

        if (!existsByUuid(courseUuid)) {
            throw new NotFoundException("Course with given uuid does not exist");
        }

        // Check if user is owner or admin
        return isOwnerOrAdmin(courseUuid, user.getUuid());
    }
}
