package com.growup.pms.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileNameUtil {

    private static final String ALLOWED_FILENAME_REGEX = "^[0-9a-zA-Z가-힣._-]+$";

    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }

    public static String getBaseName(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : filename.substring(0, lastDotIndex);
    }

    public static boolean isValidFileName(String filename) {
        if (StringUtils.isEmpty(filename)) {
            return false;
        }
        return filename.matches(ALLOWED_FILENAME_REGEX);
    }
}
