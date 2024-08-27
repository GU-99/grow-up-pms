package com.growup.pms.test.support;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.test.annotation.WithMockSecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockSecurityUserSecurityContextFactory implements WithSecurityContextFactory<WithMockSecurityUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockSecurityUser securityUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        SecurityUser principal = SecurityUser.builder()
                .id(securityUser.id())
                .username(securityUser.username())
                .password(securityUser.password())
                .build();

        var auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
