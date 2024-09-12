package com.growup.pms.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilenameUtil {

    public static String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : filename.substring(lastDotIndex + 1);
    }
}
