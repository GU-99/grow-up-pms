package com.growup.pms.test.annotation;

import com.growup.pms.test.support.ServiceMockBeanRegistrar;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ServiceMockBeanRegistrar.class)
public @interface AutoServiceMockBeans {
    @AliasFor("value")
    String basePackage() default "";

    @AliasFor("basePackage")
    String value() default "";
}
