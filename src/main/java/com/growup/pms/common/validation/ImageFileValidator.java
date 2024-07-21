package com.growup.pms.common.validation;

import com.growup.pms.common.validation.constraint.ImageFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {
    private final List<String> imageMimeTypes = Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif"
    );

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return imageMimeTypes.contains(file.getContentType());
    }
}
