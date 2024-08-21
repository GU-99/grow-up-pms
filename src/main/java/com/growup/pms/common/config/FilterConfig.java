package com.growup.pms.common.config;

import com.growup.pms.common.filter.MdcFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MdcFilter> filterRegistrationBean() {
        FilterRegistrationBean<MdcFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registrationBean.setFilter(new MdcFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}
