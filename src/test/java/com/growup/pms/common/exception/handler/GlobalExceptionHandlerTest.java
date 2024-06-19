package com.growup.pms.common.exception.handler;

import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.common.exception.exceptions.ErrorCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(GlobalExceptionHandler.class)
@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {

    @Test
    @DisplayName("EntityNotFoundException 테스트를 한다.")
    void entityNotFoundException() {
        // given & when & then
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND);
        });
    }

    @Test
    @DisplayName("AuthenticationException 테스트를 한다.")
    void authenticationException() {
        // given & when & then
        Assertions.assertThrows(AuthenticationException.class, () -> {
            throw new AuthenticationException(ErrorCode.AUTH_AUTHENTICATION_ERROR);
        });
    }

    @Test
    @DisplayName("AuthorizationException 테스트를 한다.")
    void authorizationException() {
        // given & when & then
        Assertions.assertThrows(AuthorizationException.class, () -> {
            throw new AuthorizationException(ErrorCode.AUTHZ_ACCESS_DENIED);
        });
    }
}
