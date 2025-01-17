package com.growup.pms.common.util;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthenticationUtil {

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the authenticated {@code SecurityUser} principal
     * @throws BusinessException if the current authentication is anonymous, with an {@code ACCESS_DENIED} error code
     * @see SecurityContextHolder
     * @see Authentication
     */
    public static SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        return (SecurityUser) authentication.getPrincipal();
    }
}
