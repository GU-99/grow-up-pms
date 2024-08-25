package com.growup.pms.user.service;

import com.growup.pms.auth.service.EmailVerificationService;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.storage.service.StorageService;
import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import com.growup.pms.user.service.dto.UserCreateCommand;
import com.growup.pms.user.service.dto.UserUploadCommand;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public Long save(UserCreateCommand command) {
        validateVerificationCode(command.email(), command.verificationCode());

        try {
            User user = command.toEntity();
            user.encodePassword(passwordEncoder);
            return userRepository.save(user).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public void sendVerificationCode(String email) {
        emailVerificationService.sendVerificationCode(email);
    }

    @Transactional
    public void uploadImage(Long userId, UserUploadCommand command) {
        User user = userRepository.findByIdOrThrow(userId);

        String path = "users";
        String image = storageService.upload(command.file(), path);
        user.replaceProfileImage(path + "/" + image);

        userRepository.save(user);
    }

    public List<UserSearchResponse> searchUsersByNicknamePrefix(String nicknamePrefix) {
        return userRepository.findUsersByNicknameStartingWith(nicknamePrefix);
    }

    public List<UserTeamResponse> getAllUserTeams(Long userId) {
        return userRepository.findAllUserTeams(userId);
    }

    private void validateVerificationCode(String email, int verificationCode) {
        if (!emailVerificationService.verifyAndInvalidateEmail(email, String.valueOf(verificationCode))) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_VERIFICATION);
        }
    }
}

