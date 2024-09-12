package com.growup.pms.file.service;

import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileImageService {

    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Transactional
    public void update(Long userId, MultipartFile profileImage) {
        User user = userRepository.findByIdOrThrow(userId);
        String fileName = fileStorageService.upload(profileImage);
        user.updateImageName(fileName);
    }

    @Transactional(propagation = Propagation.NEVER)
    public byte[] download(String fileName) {
        return fileStorageService.download(fileName);
    }
}
