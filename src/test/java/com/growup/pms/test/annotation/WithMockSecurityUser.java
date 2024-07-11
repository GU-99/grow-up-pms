package com.growup.pms.test.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(
        factory = WithMockSecurityUserSecurityContextFactory.class
)
public @interface WithMockSecurityUser {
    @AliasFor("email")
    String value() default "user@example.com";

    @AliasFor("value")
    String email() default "user@example.com";

    long id() default 1L;

    String password() default "password";

    @AliasFor(
            annotation = WithSecurityContext.class
    )
    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;
}
