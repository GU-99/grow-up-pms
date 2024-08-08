package com.growup.pms.user.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.storage.service.StorageService;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import com.growup.pms.user.service.dto.UserCreateCommand;
import com.growup.pms.user.service.dto.UserUploadCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Transactional
    public Long save(UserCreateCommand command) {
        try {
            User user = command.toEntity();
            user.encodePassword(passwordEncoder);
            return userRepository.save(user).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException(ErrorCode.ENTITY_ALREADY_EXIST);
        }
    }

    @Transactional
    public void uploadImage(Long userId, UserUploadCommand command) {
        User user = userRepository.findByIdOrThrow(userId);

        String path = "users";
        String image = storageService.upload(command.file(), path);
        user.updateImage(path + "/" + image);

        userRepository.save(user);
    }
}

