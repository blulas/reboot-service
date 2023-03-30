package com.att.infrastructure.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.DispatcherType;

import com.att.infrastructure.data.IDataAccess;
import com.att.infrastructure.data.DataAccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
@ServletComponentScan
public class ServiceConfiguration {                                    

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfiguration.class);
    private final ServiceFilter serviceFilter;

    @Autowired
    public ServiceConfiguration(ServiceFilter serviceFilter) {
        this.serviceFilter = serviceFilter;
    }

    @Bean
    public FilterRegistrationBean<ServiceFilter> serviceFilterRegistration() {

        FilterRegistrationBean<ServiceFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(serviceFilter);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public IDataAccess dataAccessService() {
        return new DataAccess();
    }
}
