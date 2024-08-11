package com.growup.pms.user.service;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.storage.service.StorageService;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import com.growup.pms.user.service.dto.UserCreateCommand;
import com.growup.pms.user.service.dto.UserDownloadCommand;
import com.growup.pms.user.service.dto.UserUploadCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        MultipartFile image = command.file();

        String imagePath = storageService.upload(image, path);
        user.updateImage(path + "/" + imagePath);
        user.updateImageName(image.getOriginalFilename());

        userRepository.save(user);
    }

    @Transactional
    public UserDownloadCommand imageDownload(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);

        String path = user.getProfile().getImage();

        return new UserDownloadCommand(user.getProfile().getImageName(), storageService.getFileResource(path));
    }
}

