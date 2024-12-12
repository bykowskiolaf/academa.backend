package dev.bykowski.academa.controllers;

import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Course.CreateCourseDTO;
import dev.bykowski.academa.dtos.Course.FullCourseDTO;
import dev.bykowski.academa.exceptions.ForbiddenActionException;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.services.CourseService;
import dev.bykowski.academa.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<CourseDTO>> getCourses(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        Page<CourseDTO> courses = courseService.getPaginatedCourses(pageNumber, pageSize);
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }

    @PostMapping
    @RolesAllowed("INSTRUCTOR")
    public ResponseEntity<CourseDTO> createCourse(
            @Valid @RequestBody CreateCourseDTO course
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User instructor = userService.getByEmail(auth.getName());

        CourseDTO savedCourse = courseService.createCourse(course, instructor.getUuid());
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<FullCourseDTO> getCourseByUuid(@PathVariable UUID uuid) {
        FullCourseDTO course = courseService.getByUuid(uuid);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @PutMapping("/{uuid}")
    @RolesAllowed("INSTRUCTOR")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable UUID uuid, @Valid @RequestBody CreateCourseDTO course) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User instructor = userService.getByEmail(auth.getName());

        if (!courseService.existsByUuid(uuid)) {
            throw new NotFoundException("Course with given uuid does not exist");
        }

        if (!courseService.isOwnerOrAdmin(uuid, instructor.getUuid())) {
            throw new ForbiddenActionException("You do not have permission to edit this course");
        }

        CourseDTO updatedCourse = courseService.updateCourse(uuid, course);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    @DeleteMapping("/{uuid}")
    @RolesAllowed("INSTRUCTOR")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID uuid) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User instructor = userService.getByEmail(auth.getName());

        if (!courseService.existsByUuid(uuid)) {
            throw new NotFoundException("Course with given uuid does not exist");
        }

        if (!courseService.isOwnerOrAdmin(uuid, instructor.getUuid())) {
            throw new ForbiddenActionException("You do not have permission to delete this course");
        }

        courseService.deleteCourse(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{studentUuid}/courses")
    @RolesAllowed("INSTRUCTOR")
    public ResponseEntity<List<CourseDTO>> getStudentCourses(@PathVariable UUID studentUuid) {
        List<CourseDTO> studentCourses = courseService.getStudentCourses(studentUuid);
        return new ResponseEntity<>(studentCourses, HttpStatus.OK);
    }

    @PostMapping("/{studentUuid}/courses/{courseUuid}")
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<List<CourseDTO>> assignCourse(@PathVariable UUID studentUuid, @PathVariable UUID courseUuid) {
        List<CourseDTO> studentCourses = courseService.assignCourse(studentUuid, courseUuid);
        return new ResponseEntity<>(studentCourses, HttpStatus.OK);
    }

    @DeleteMapping("/{studentUuid}/courses/{courseUuid}")
    @RolesAllowed({"ADMIN", "INSTRUCTOR"})
    public ResponseEntity<Void> unAssignStudent(@PathVariable UUID studentUuid, @PathVariable UUID courseUuid) {
        courseService.unassignCourse(studentUuid, courseUuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}