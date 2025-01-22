package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.User.RegisterUserDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.Student;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.repositories.StudentRepository;
import dev.bykowski.academa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StudentRepository studentRepository;

    private UUID userUuid;

    private String email;

    private String encodedPassword;

    private String plainPassword;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        userUuid = UUID.randomUUID();
        email = "user@example.com";
        plainPassword = "plainPassword123";
        encodedPassword = "encodedPassword123";
    }

    @Test
    void shouldRegisterUser() {
        // Arrange
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .email(email)
                .password(plainPassword)
                .confirmPassword(plainPassword)
                .givenName("John")
                .familyName("Doe")
                .build();

        Student savedStudent = Student.builder()
                .email(registerUserDTO.getEmail())
                .password(encodedPassword)
                .givenName(registerUserDTO.getGivenName())
                .familyName(registerUserDTO.getFamilyName())
                .role(Role.STUDENT)
                .build();

        when(userRepository.findByEmail(registerUserDTO.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(registerUserDTO.getPassword())).thenReturn(encodedPassword);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // Act
        userService.register(registerUserDTO);

        // Assert
        verify(userRepository, times(1)).findByEmail(registerUserDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(plainPassword);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void shouldNotRegisterUserIfEmailExists() {
        RegisterUserDTO registerUserDTO = RegisterUserDTO.builder()
                .email(email)
                .password(plainPassword)
                .build();

        when(userRepository.findByEmail(registerUserDTO.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> userService.register(registerUserDTO));


        verify(userRepository, times(1)).findByEmail(registerUserDTO.getEmail());
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldCheckIfUserIsAdmin() {
        User user = User.builder()
                .uuid(userUuid)
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        assertTrue(userService.isAdmin(userUuid));

        verify(userRepository, times(1)).findById(userUuid);
    }

    @Test
    void shouldReturnFalseIfUserIsNotAdmin() {
        User user = User.builder()
                .uuid(userUuid)
                .role(Role.STUDENT)
                .build();

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        assertFalse(userService.isAdmin(userUuid));

        verify(userRepository, times(1)).findById(userUuid);
    }

    @Test
    void shouldThrowNotFoundExceptionIfUserNotFoundForIsAdmin() {
        when(userRepository.findById(userUuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.isAdmin(userUuid));

        verify(userRepository, times(1)).findById(userUuid);
    }

    @Test
    void shouldGetUserByEmail() {
        User user = User.builder()
                .uuid(userUuid)
                .email(email)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.getByEmail(email);

        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundByEmail() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getByEmail(email));

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void shouldGetUserByUuid() {
        User user = User.builder()
                .uuid(userUuid)
                .email(email)
                .build();

        when(userRepository.findById(userUuid)).thenReturn(Optional.of(user));

        User foundUser = userService.getByUuid(userUuid);

        assertNotNull(foundUser);
        assertEquals(userUuid, foundUser.getUuid());

        verify(userRepository, times(1)).findById(userUuid);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUserNotFoundByUuid() {
        when(userRepository.findById(userUuid)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getByUuid(userUuid));

        verify(userRepository, times(1)).findById(userUuid);
    }

    @Test
    void shouldLoadUserByUsername() {
        User user = User.builder()
                .uuid(userUuid)
                .email(email)
                .password(encodedPassword)
                .role(Role.STUDENT)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(encodedPassword, userDetails.getPassword());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFoundByUsername() {
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));

        verify(userRepository, times(1)).findByEmail(email);
    }
}