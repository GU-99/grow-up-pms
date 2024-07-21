package com.growup.pms.user.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.storage.service.StorageService;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.dto.UserCreateRequest;
import com.growup.pms.user.dto.UserUploadRequest;
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
    public Long save(UserCreateRequest request) {
        try {
            User user = UserCreateRequest.toEntity(request);
            user.encodePassword(passwordEncoder);
            return userRepository.save(user).getId();
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateException(ErrorCode.ENTITY_ALREADY_EXIST);
        }
    }

    public void imageUpload(UserUploadRequest userUploadRequest) {
        User user = userRepository.findByIdOrThrow(userUploadRequest.getUserId());

        String path = "users";
        String image = storageService.upload(userUploadRequest.getFile(), path);
        user.updateImage(path + "/" + image);

        userRepository.save(user);
    }
}

