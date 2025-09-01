package com.bachir9.app.auth.impl;

import com.bachir9.app.auth.AuthenticationService;
import com.bachir9.app.auth.request.AuthenticationRequest;
import com.bachir9.app.auth.request.RefreshRequest;
import com.bachir9.app.auth.request.RegistrationRequest;
import com.bachir9.app.auth.response.AuthenticationResponse;
import com.bachir9.app.exception.BusinessException;
import com.bachir9.app.role.Role;
import com.bachir9.app.role.RoleRepository;
import com.bachir9.app.security.JwtService;
import com.bachir9.app.user.User;
import com.bachir9.app.user.UserMapper;
import com.bachir9.app.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.bachir9.app.exception.ErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public AuthenticationResponse login(final AuthenticationRequest request) {
        final var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        final var user = (User) auth.getPrincipal();
        final var token = this.jwtService.generateAccessToken(user.getUsername());
        final var refreshToken = this.jwtService.generateRefreshToken(user.getUsername());
        final var tokenType = "Bearer";

        return AuthenticationResponse.builder()
                .accessToken(token).refreshToken(refreshToken).tokenType(tokenType).build();
    }

    @Override
    @Transactional
    public void register(final RegistrationRequest request) {
        checkUserEmail(request.getEmail());
        checkUserPhoneNumber(request.getPhoneNumber());
        checkPasswords(request.getPassword(), request.getConfirmPassword());

        final var userRole = this.roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Role user does not exist"));
        final var roles = new ArrayList<Role>();
        roles.add(userRole);

        final var user = this.userMapper.toUser(request);
        user.setRoles(roles);
        log.debug("Saving user {}", user);

        this.userRepository.save(user);

        final var users = new ArrayList<User>();
        users.add(user);
        userRole.setUsers(users);

        this.roleRepository.save(userRole);


    }

    @Override
    public AuthenticationResponse refreshToken(final RefreshRequest request) {
        final var newAccessToken = this.jwtService.refreshAccessToken(request.getRefreshToken());
        final var tokenType = "Bearer";
        return AuthenticationResponse.builder().accessToken(newAccessToken).refreshToken(request.getRefreshToken()).tokenType(tokenType).build();
    }

    private void checkUserEmail(final String email) {
        final boolean emailExists = this.userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            throw new BusinessException(EMAIL_ALREADY_EXISTS);
        }
    }

    private void checkPasswords(final String password,
                                final String confirmPassword) {
        if (password == null || !password.equals(confirmPassword)) {
            throw new BusinessException(PASSWORD_MISMATCH);
        }
    }

    private void checkUserPhoneNumber(final String phoneNumber) {
        final boolean phoneNumberExists = this.userRepository.existsByPhoneNumber(phoneNumber);
        if (phoneNumberExists) {
            throw new BusinessException(PHONE_ALREADY_EXISTS);
        }
    }
}
