package com.growup.pms.test.annotation;

import com.growup.pms.auth.domain.SecurityUser;
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
                .email(securityUser.email())
                .password(securityUser.password())
                .build();

        var auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
