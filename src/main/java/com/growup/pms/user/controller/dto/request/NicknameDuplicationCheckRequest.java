package com.growup.pms.user.controller.dto.request;

import static com.growup.pms.common.constant.RegexConstants.NICKNAME_PATTERN;

import com.growup.pms.user.service.dto.NicknameDuplicationCheckCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record NicknameDuplicationCheckRequest(
        @NotNull
        @Pattern(regexp = NICKNAME_PATTERN)
        String nickname
) {

    public NicknameDuplicationCheckCommand toCommand() {
        return NicknameDuplicationCheckCommand.builder()
                .nickname(nickname)
                .build();
    }
}
