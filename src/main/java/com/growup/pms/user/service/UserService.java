package com.growup.pms.user.service;

import com.growup.pms.auth.service.RedisEmailVerificationService;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.storage.service.StorageService;
import com.growup.pms.user.controller.dto.response.RecoverPasswordResponse;
import com.growup.pms.user.controller.dto.response.RecoverUsernameResponse;
import com.growup.pms.user.controller.dto.response.UserResponse;
import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import com.growup.pms.user.controller.dto.response.UserUpdateResponse;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import com.growup.pms.user.service.dto.PasswordUpdateCommand;
import com.growup.pms.user.service.dto.RecoverPasswordCommand;
import com.growup.pms.user.service.dto.RecoverUsernameCommand;
import com.growup.pms.user.service.dto.UserCreateCommand;
import com.growup.pms.user.service.dto.UserDownloadCommand;
import com.growup.pms.user.service.dto.UserLinksUpdateCommand;
import com.growup.pms.user.service.dto.UserUpdateCommand;
import com.growup.pms.user.service.dto.UserUploadCommand;
import com.growup.pms.user.service.dto.VerificationCodeCreateCommand;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final RedisEmailVerificationService emailVerificationService;

    public UserResponse getUser(Long userId) {
        User foundUser = userRepository.findByIdOrThrow(userId);
        return UserResponse.from(foundUser);
    }

    @Transactional
    public Long save(UserCreateCommand command) {
        validateVerificationCode(command.email(), command.verificationCode());
        try {
            User user = command.toEntity();
            user.encodePassword(passwordEncoder);
            return userRepository.save(user).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public void sendVerificationCode(VerificationCodeCreateCommand command) {
        emailVerificationService.sendVerificationCode(command.email());
    }

    @Transactional
    public void uploadImage(Long userId, UserUploadCommand command) {
        User user = userRepository.findByIdOrThrow(userId);

        String path = "users";
        MultipartFile image = command.file();

        String imagePath = storageService.upload(image, path);
        user.replaceProfileImage(path + "/" + imagePath);
        user.updateImageName(image.getOriginalFilename());

        userRepository.save(user);
    }

    @Transactional
    public UserDownloadCommand imageDownload(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);

        String path = user.getProfile().getImage();

        return new UserDownloadCommand(user.getProfile().getImageName(), storageService.getFileResource(path));
    }

    @Transactional
    public void updatePassword(Long userId, PasswordUpdateCommand command) {
        User user = userRepository.findByIdOrThrow(userId);
        validateCurrentPassword(user.getPassword(), command.password());

        user.changePassword(passwordEncoder, command.newPassword());
    }


    @Transactional
    public UserUpdateResponse updateUserDetails(Long userId, UserUpdateCommand command) {
        User user = userRepository.findByIdOrThrow(userId);

        editFieldIfPresent(command.nickname(), (v, u) -> u.editNickname(v.get()), user);
        editFieldIfPresent(command.bio(), (v, u) -> u.editBio(v.get()), user);
        editFieldIfPresent(command.profileImageUrl(), (v, u) -> u.editProfileImageUrl(v.get()), user);

        return UserUpdateResponse.of(user);
    }

    @Transactional
    public void updateUserLinks(Long userId, UserLinksUpdateCommand command) {
        User user = userRepository.findByIdOrThrow(userId);

        editFieldIfPresent(command.links(), (v, u) -> u.editLinks(v.get()), user);
    }

    private <T> void editFieldIfPresent(JsonNullable<T> value, BiConsumer<JsonNullable<T>, User> updater, User user) {
        value.ifPresent(v -> updater.accept(JsonNullable.of(v), user));
    }

    public RecoverUsernameResponse recoverUsername(RecoverUsernameCommand command) {
        validateVerificationCode(command.email(), command.verificationCode());

        User user = userRepository.findByEmailOrThrow(command.email());

        return new RecoverUsernameResponse(user.getUsername());
    }

    @Transactional
    public RecoverPasswordResponse recoverPassword(RecoverPasswordCommand command) {
        validateVerificationCode(command.email(), command.verificationCode());

        User user = userRepository.findByEmailOrThrow(command.email());

        validateUsernameMatch(command.username(), user.getUsername());

        String temporaryPassword = user.renewPassword(passwordEncoder);
        return new RecoverPasswordResponse(temporaryPassword);
    }

    private void validateUsernameMatch(String inputUsername, String storedUsername) {
        if (!storedUsername.equals(inputUsername)) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }

    private void validateCurrentPassword(String inputPassword, String storedPassword) {
        if (!passwordEncoder.matches(inputPassword, storedPassword)) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    public List<UserSearchResponse> searchUsersByNicknamePrefix(String nicknamePrefix) {
        return userRepository.findUsersByNicknameStartingWith(nicknamePrefix);
    }

    public List<UserTeamResponse> getAllUserTeams(Long userId) {
        return userRepository.findAllUserTeams(userId);
    }

    private void validateVerificationCode(String email, String verificationCode) {
        if (!emailVerificationService.verifyAndInvalidateEmail(email, verificationCode)) {
            throw new BusinessException(ErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
        }
    }
}
