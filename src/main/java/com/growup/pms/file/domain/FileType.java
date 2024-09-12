package com.growup.pms.file.domain;

import lombok.Getter;

@Getter
public enum FileType {
    IMAGE(new String[]{"jpg", "jpeg", "png", "svg", "webp"}),
    DOCUMENT(new String[]{"pdf", "txt", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "hwp"}),
    ARCHIVE(new String[]{"zip", "rar", "7z", "alz", "egg"});

    private final String[] extensions;

    FileType(String[] extensions) {
        this.extensions = extensions;
    }
}
