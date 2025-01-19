package dev.bykowski.academa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bykowski.academa.config.WebSecurityConfig;
import dev.bykowski.academa.dtos.Course.CourseDTO;
import dev.bykowski.academa.dtos.Course.CreateCourseDTO;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.services.CourseService;
import dev.bykowski.academa.services.CustomOAuth2UserService;
import dev.bykowski.academa.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@ActiveProfiles("test")
@Import(WebSecurityConfig.class)
class CourseControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR", username = "test@bykowski.dev")
    void shouldCreateCourse() throws Exception {
        CreateCourseDTO createCourseDTO = new CreateCourseDTO("New Course", "Course Description");
        CourseDTO courseDTO = new CourseDTO(UUID.randomUUID(), createCourseDTO.getName(), createCourseDTO.getDescription());

        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .build();

        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);
        Mockito.when(courseService.createCourse(any(CreateCourseDTO.class), eq(instructor.getUuid()))).thenReturn(courseDTO);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(courseDTO.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(courseDTO.getName()))
                .andExpect(jsonPath("$.description").value(courseDTO.getDescription()));
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR", username = "test@bykowski.dev")
    void shouldUpdateCourseAsOwner() throws Exception {
        UUID courseId = UUID.randomUUID();
        CreateCourseDTO updateCourseDTO = new CreateCourseDTO("Updated Course", "Updated Description");
        CourseDTO updatedCourseDTO = new CourseDTO(courseId, updateCourseDTO.getName(), updateCourseDTO.getDescription());

        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .build();

        Mockito.when(courseService.existsByUuid(courseId)).thenReturn(true);
        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);
        Mockito.when(courseService.isOwnerOrAdmin(eq(courseId), eq(instructor.getUuid()))).thenReturn(true);
        Mockito.when(courseService.updateCourse(eq(courseId), any(CreateCourseDTO.class))).thenReturn(updatedCourseDTO);

        mockMvc.perform(put("/courses/{uuid}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCourseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(updatedCourseDTO.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(updatedCourseDTO.getName()))
                .andExpect(jsonPath("$.description").value(updatedCourseDTO.getDescription()));
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR", username = "test@bykowski.dev")
    void shouldReturnBadRequestWhenCreateCourseWithInvalidData() throws Exception {
        // Empty name and description to trigger validation errors
        CreateCourseDTO invalidCourseDTO = new CreateCourseDTO("", "");

        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .build();

        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCourseDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldReturnForbiddenWhenStudentTriesToCreateCourse() throws Exception {
        CreateCourseDTO createCourseDTO = new CreateCourseDTO("New Course", "Description");

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR", username = "test@bykowski.dev")
    void shouldFailToUpdateCourseIfNotOwner() throws Exception {
        UUID courseId = UUID.randomUUID();
        CreateCourseDTO updateCourseDTO = new CreateCourseDTO("Updated Course", "Updated Description");

        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .build();

        Mockito.when(courseService.existsByUuid(courseId)).thenReturn(true);
        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);
        Mockito.when(courseService.isOwnerOrAdmin(eq(courseId), eq(instructor.getUuid()))).thenReturn(false);

        mockMvc.perform(put("/courses/{uuid}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCourseDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldReturnForbiddenWhenStudentTriesToUpdateCourse() throws Exception {
        UUID courseId = UUID.randomUUID();
        CreateCourseDTO updateCourseDTO = new CreateCourseDTO("Updated Course", "Updated Description");

        mockMvc.perform(put("/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCourseDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR", username = "test@bykowski.dev")
    void shouldReturnNotFoundWhenUpdatingNonExistentCourse() throws Exception {
        UUID nonExistentCourseId = UUID.randomUUID();
        CreateCourseDTO updateCourseDTO = new CreateCourseDTO("Updated Course", "Updated Description");

        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .role(Role.INSTRUCTOR)
                .build();

        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);

        mockMvc.perform(put("/courses/{id}", nonExistentCourseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCourseDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@bykowski.dev", roles = "INSTRUCTOR")
    void shouldReturnNotFoundWhenDeletingNonExistentCourse() throws Exception {
        UUID nonExistentCourseId = UUID.randomUUID();

        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .build();

        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);

        mockMvc.perform(delete("/courses/{id}", nonExistentCourseId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR", username = "test@bykowski.dev")
    void shouldDeleteCourseAsOwner() throws Exception {
        UUID courseId = UUID.randomUUID();

        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .build();

        Mockito.when(courseService.existsByUuid(courseId)).thenReturn(true);
        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);
        Mockito.when(courseService.isOwnerOrAdmin(eq(courseId), eq(instructor.getUuid()))).thenReturn(true);

        mockMvc.perform(delete("/courses/{uuid}", courseId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR", username = "test@bykowski.dev")
    void shouldFailToDeleteCourseIfNotOwner() throws Exception {
        UUID courseId = UUID.randomUUID();
        User instructor = User.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .build();

        Mockito.when(courseService.existsByUuid(courseId)).thenReturn(true);
        Mockito.when(userService.getByEmail("test@bykowski.dev")).thenReturn(instructor);
        Mockito.when(courseService.isOwnerOrAdmin(eq(courseId), eq(instructor.getUuid()))).thenReturn(false);

        mockMvc.perform(delete("/courses/{uuid}", courseId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    void shouldReturnForbiddenWhenStudentTriesToDeleteCourse() throws Exception {
        UUID courseId = UUID.randomUUID();

        mockMvc.perform(delete("/courses/{id}", courseId))
                .andExpect(status().isForbidden());
    }
}