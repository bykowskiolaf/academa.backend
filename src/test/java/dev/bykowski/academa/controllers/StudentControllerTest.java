package dev.bykowski.academa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bykowski.academa.config.WebSecurityConfig;
import dev.bykowski.academa.dtos.Student.CreateStudentDTO;
import dev.bykowski.academa.dtos.Student.StudentDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.services.CustomOAuth2UserService;
import dev.bykowski.academa.services.StudentService;
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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@ActiveProfiles("test")
@Import(WebSecurityConfig.class)
class StudentControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

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
    @WithMockUser(roles = "ADMIN")
    void shouldCreateStudent() throws Exception {
        CreateStudentDTO createStudentDTO = CreateStudentDTO.builder()
                .givenName("John")
                .familyName("Doe")
                .email("test@bykowski.dev")
                .password("password")
                .confirmPassword("password")
                .role(Role.STUDENT.getName())
                .build();

        StudentDTO studentDTO = StudentDTO.builder()
                .uuid(UUID.randomUUID())
                .email("test@bykowski.dev")
                .givenName("John")
                .familyName("Doe")
                .role(Role.STUDENT.getName())
                .build();

        Mockito.when(studentService.create(any(CreateStudentDTO.class))).thenReturn(studentDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createStudentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uuid").value(studentDTO.getUuid().toString()))
                .andExpect(jsonPath("$.givenName").value(studentDTO.getGivenName()))
                .andExpect(jsonPath("$.familyName").value(studentDTO.getFamilyName()))
                .andExpect(jsonPath("$.email").value(studentDTO.getEmail()))
                .andExpect(jsonPath("$.role").value(Role.STUDENT.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllStudents() throws Exception {
        StudentDTO student = StudentDTO.builder()
                .uuid(UUID.randomUUID())
                .givenName("John")
                .familyName("Doe")
                .build();

        List<StudentDTO> students = Collections.singletonList(student);

        Mockito.when(studentService.getAll()).thenReturn(students);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").value(student.getUuid().toString()))
                .andExpect(jsonPath("$[0].givenName").value(student.getGivenName()))
                .andExpect(jsonPath("$[0].familyName").value(student.getFamilyName()));
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR")
    void shouldReturnStudentByUuid() throws Exception {
        UUID studentUuid = UUID.randomUUID();

        StudentDTO student = StudentDTO.builder()
                .uuid(studentUuid)
                .givenName("John")
                .familyName("Doe")
                .build();

        Mockito.when(studentService.getByUuid(studentUuid)).thenReturn(student);

        mockMvc.perform(get("/students/{uuid}", studentUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(student.getUuid().toString()))
                .andExpect(jsonPath("$.givenName").value(student.getGivenName()))
                .andExpect(jsonPath("$.familyName").value(student.getFamilyName()));
    }

    @Test
    @WithMockUser(roles = "INSTRUCTOR")
    void shouldReturnNotFoundWhenStudentDoesNotExist() throws Exception {
        UUID nonExistentUuid = UUID.randomUUID();

        Mockito.when(studentService.getByUuid(nonExistentUuid))
                .thenThrow(new NotFoundException("Student not found"));

        mockMvc.perform(get("/students/{uuid}", nonExistentUuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateStudent() throws Exception {
        UUID studentUuid = UUID.randomUUID();

        CreateStudentDTO updateStudentDTO = CreateStudentDTO.builder()
                .givenName("Jane")
                .familyName("Doe")
                .email("test@bykowski.dev")
                .password("password")
                .confirmPassword("password")
                .role(Role.STUDENT.getName())
                .build();

        StudentDTO updatedStudentDTO = StudentDTO.builder()
                .uuid(studentUuid)
                .email("test@bykowski.dev")
                .givenName("Jane")
                .familyName("Doe")
                .role(Role.STUDENT.getName())
                .build();

        Mockito.when(studentService.update(eq(studentUuid), any(CreateStudentDTO.class)))
                .thenReturn(updatedStudentDTO);

        mockMvc.perform(put("/students/{uuid}", studentUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(updatedStudentDTO.getUuid().toString()))
                .andExpect(jsonPath("$.givenName").value(updatedStudentDTO.getGivenName()))
                .andExpect(jsonPath("$.familyName").value(updatedStudentDTO.getFamilyName()))
                .andExpect(jsonPath("$.email").value(updatedStudentDTO.getEmail()))
                .andExpect(jsonPath("$.role").value(Role.STUDENT.getName()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteStudent() throws Exception {
        UUID studentUuid = UUID.randomUUID();

        mockMvc.perform(delete("/students/{uuid}", studentUuid))
                .andExpect(status().isNoContent());

        Mockito.verify(studentService).delete(studentUuid);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnNotFoundWhenDeletingNonExistentStudent() throws Exception {
        UUID nonExistentUuid = UUID.randomUUID();

        Mockito.doThrow(new NotFoundException("Student not found"))
                .when(studentService).delete(nonExistentUuid);

        mockMvc.perform(delete("/students/{uuid}", nonExistentUuid))
                .andExpect(status().isNotFound());
    }
}