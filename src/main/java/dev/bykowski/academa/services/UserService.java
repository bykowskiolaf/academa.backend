package dev.bykowski.academa.services;

import dev.bykowski.academa.dtos.User.RegisterUserDTO;
import dev.bykowski.academa.exceptions.NotFoundException;
import dev.bykowski.academa.models.User.Role;
import dev.bykowski.academa.models.User.User;
import dev.bykowski.academa.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(RegisterUserDTO userDTO) {
        boolean alreadyExists = userRepository.findByEmail(userDTO.getEmail()).isPresent();

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encodedPassword);

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .givenName(userDTO.getGivenName())
                .familyName(userDTO.getFamilyName())
                .picture(userDTO.getPicture())
                .locale(userDTO.getLocale())
                .role(Role.STUDENT)
                .build();

        if (!alreadyExists)
            userRepository.save(user);
    }

    public Boolean isAdmin(UUID userUuid) {
        User user = userRepository.findById(userUuid)
                .orElseThrow(() -> new NotFoundException("User with given uuid not found"));
        return user.getRole().equals(Role.ADMIN);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with given email not found"));
    }

    public User getByUuid(UUID uuid) {
        return userRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundException("User with given uuid not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + username));

        Set<GrantedAuthority> authorities = new HashSet<>(user.getAuthorities());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
