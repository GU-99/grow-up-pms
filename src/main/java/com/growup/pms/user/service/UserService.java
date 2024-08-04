package com.growup.pms.user.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.storage.service.StorageService;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.dto.UserCreateDto;
import com.growup.pms.user.dto.UserUploadDto;
import com.growup.pms.user.repository.UserRepository;
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
    public Long save(UserCreateDto request) {
        try {
            User user = UserCreateDto.toEntity(request);
            user.encodePassword(passwordEncoder);
            return userRepository.save(user).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException(ErrorCode.ENTITY_ALREADY_EXIST);
        }
    }

    @Transactional
    public void uploadImage(Long userId, UserUploadDto request) {
        User user = userRepository.findByIdOrThrow(userId);

        String path = "users";
        String image = storageService.upload(request.getFile(), path);
        user.updateImage(path + "/" + image);

        userRepository.save(user);
    }
}

