package com.growup.pms.auth.controller.dto.request;

import com.growup.pms.auth.service.dto.oauth.OauthUserLoginCommand;
import lombok.Builder;

@Builder
public record OauthLoginRequest(String code)
{
    public OauthUserLoginCommand toCommand() {
        return  OauthUserLoginCommand.builder()
                .code(code)
                .build();
    }
}


