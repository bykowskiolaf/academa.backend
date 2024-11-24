package dev.bykowski.academa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bykowski.academa.config.WebSecurityConfig;
import dev.bykowski.academa.dtos.User.LoginDTO;
import dev.bykowski.academa.dtos.User.RegisterUserDTO;
import dev.bykowski.academa.dtos.User.UserDTO;
import dev.bykowski.academa.exceptions.AlreadyExistsException;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.services.CustomOAuth2UserService;
import dev.bykowski.academa.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
@Import(WebSecurityConfig.class)
public class AuthControllerTest {

    private static final String TEST_EMAIL = "test@bykowski.dev";

    private static final String TEST_PASSWORD = "password";

    private static final String WRONG_PASSWORD = "wrongpassword";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterUserDTO registerUserDTO;

    private LoginDTO loginDTO;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setEmail(TEST_EMAIL);
        registerUserDTO.setPassword(TEST_PASSWORD);
        registerUserDTO.setConfirmPassword(TEST_PASSWORD);
        registerUserDTO.setGivenName("Test");
        registerUserDTO.setFamilyName("User");

        loginDTO = new LoginDTO();
        loginDTO.setEmail(TEST_EMAIL);
        loginDTO.setPassword(TEST_PASSWORD);

        userDTO = new UserDTO();
        userDTO.setEmail(TEST_EMAIL);
        userDTO.setGivenName("Test");
        userDTO.setFamilyName("User");
        userDTO.setRole(Role.STUDENT.getName());
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        when(userService.register(any(RegisterUserDTO.class))).thenReturn(userDTO);


        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.givenName").value("Test"))
                .andExpect(jsonPath("$.familyName").value("User"))
                .andExpect(jsonPath("$.role").value(Role.STUDENT.getName()));

        ArgumentCaptor<RegisterUserDTO> registerCaptor = ArgumentCaptor.forClass(RegisterUserDTO.class);
        verify(userService).register(registerCaptor.capture());
        assertEquals(registerUserDTO.getEmail(), registerCaptor.getValue().getEmail());
    }

    @Test
    public void testRegisterEmailAlreadyExists() throws Exception {
        when(userService.register(any(RegisterUserDTO.class)))
                .thenThrow(new AlreadyExistsException("User with given email already exists"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testLoginSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        User user = User.builder()
                .email(TEST_EMAIL)
                .givenName("Test")
                .familyName("User")
                .role(Role.STUDENT)
                .build();

        when(userService.getByEmail(TEST_EMAIL)).thenReturn(user);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.givenName").value("Test"))
                .andExpect(jsonPath("$.familyName").value("User"))
                .andExpect(jsonPath("$.role").value(Role.STUDENT.getName()));

        ArgumentCaptor<UsernamePasswordAuthenticationToken> authCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(authCaptor.capture());
        assertEquals(TEST_EMAIL, authCaptor.getValue().getPrincipal());
        assertEquals(TEST_PASSWORD, authCaptor.getValue().getCredentials());
    }

    @Test
    public void testLoginBadCredentials() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        loginDTO.setPassword(WRONG_PASSWORD);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());

        verifyNoInteractions(userService);
    }

    @Test
    public void testRegisterInvalidData() throws Exception {
        registerUserDTO.setEmail(""); // Invalid email

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginDisabledException() throws Exception {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("User is disabled"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("User is disabled"));
    }

    @Test
    public void testLogoutNoAuthentication() throws Exception {
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());
    }
}