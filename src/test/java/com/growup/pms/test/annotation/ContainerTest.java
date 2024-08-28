package com.growup.pms.test.annotation;

import com.growup.pms.test.support.TestcontainersInitializer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.ContextConfiguration;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(initializers = TestcontainersInitializer.class)
public @interface ContainerTest {
}
