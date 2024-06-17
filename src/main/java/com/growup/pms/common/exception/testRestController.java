package com.growup.pms.common.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testRestController {

    @GetMapping("/entity")
    public void entityException() {
        throw new EntityNotFoundException(ErrorCode.ENTITY_NOT_FOUND);
    }

    @GetMapping("/authen")
    public void authenticationException() {
        throw new EntityNotFoundException(ErrorCode.AUTH_AUTHENTICATION_ERROR);
    }

    @GetMapping("/author")
    public void authorizationException() {
        throw new AuthorizationException(ErrorCode.AUTHZ_ACCESS_DENIED);
    }
}
