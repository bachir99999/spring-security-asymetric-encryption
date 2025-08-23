package com.bachir9.app.user.impl;

import com.bachir9.app.exception.BusinessException;
import com.bachir9.app.user.UserMapper;
import com.bachir9.app.user.UserRepository;
import com.bachir9.app.user.UserService;
import com.bachir9.app.user.request.ChangePasswordRequest;
import com.bachir9.app.user.request.ProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.bachir9.app.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void updateProfileInfo(ProfileUpdateRequest request, String userId) {
        final var savedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        this.userMapper.mergeUserInfo(savedUser, request);
        this.userRepository.save(savedUser);

    }

    @Override
    public void changePassword(ChangePasswordRequest request, String userId) {
        if (!request.getConfirmPassword().equals(request.getNewPassword())) {
            throw new BusinessException(CHANGE_PASSWORD_MISMATCH);
        }

        final var savedUser = this.userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getOldPassword(), savedUser.getPassword())) {
            throw new BusinessException(INVALID_CURRENT_PASSWORD);
        }

        final var encodedPassword = this.passwordEncoder.encode(request.getNewPassword());
        savedUser.setPassword(encodedPassword);
        this.userRepository.save(savedUser);
    }

    @Override
    public void deactivateAccount(String userId) {
        final var user = this.userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        if(!user.isEnabled()){
            throw new BusinessException(ACCOUNT_ALREADY_DEACTIVATED);
        }

        user.setEnabled(false);
        this.userRepository.save(user);
    }

    @Override
    public void reactivateAccount(String userId) {
        final var user = this.userRepository.findById(userId).orElseThrow(() -> new BusinessException(USER_NOT_FOUND));
        if(user.isEnabled()){
            throw new BusinessException(ACCOUNT_ALREADY_ENABLED);
        }

        user.setEnabled(true);
        this.userRepository.save(user);
    }

    @Override
    public void deleteAccount(String userId) {


    }

    @Override
    public UserDetails loadUserByUsername(final String userEmail) throws UsernameNotFoundException {
        return this.userRepository.findByEmailIgnoreCase(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userEmail));
    }
}
