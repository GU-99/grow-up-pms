package com.growup.pms.common.validator;

import com.growup.pms.common.util.FilenameUtil;
import com.growup.pms.common.validator.annotation.File;
import com.growup.pms.file.domain.FileType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.utils.StringUtils;

public class FileValidator implements ConstraintValidator<File, MultipartFile> {
    private Set<String> allowedExtensions;

    @Override
    public void initialize(File constraintAnnotation) {
        this.allowedExtensions = new HashSet<>();
        for (FileType type : constraintAnnotation.types()) {
            allowedExtensions.addAll(Arrays.asList(type.getExtensions()));
        }
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty() || StringUtils.isEmpty(file.getOriginalFilename())) {
            return false;
        }

        String extension = FilenameUtil.getFileExtension(file.getOriginalFilename());
        return allowedExtensions.contains(extension.toLowerCase());
    }
}
