package com.gdg_team9.SafePlate.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class FeignConfig {
    /**
     * 동시에 여러 스레드에서 Feign client(AI서버 호출 로직)를 사용할 때,
     * HttpMessageConverter가 초기화되지 않아 발생하는 문제를 방지하기 위한 Bean 정의
     * <p>
     * 자세한 내용은 다음 Issue 참고:
     * <a href="https://github.com/spring-cloud/spring-cloud-openfeign/issues/1307">
     * https://github.com/spring-cloud/spring-cloud-openfeign/issues/1307
     * </a>
     */
    @Bean
    @ConditionalOnMissingBean
    public FeignHttpMessageConverters feignHttpMessageConverters(
            ObjectProvider<HttpMessageConverter<?>> messageConverters,
            ObjectProvider<HttpMessageConverterCustomizer> customizers) {
        var feignHttpMessageConverters = new FeignHttpMessageConverters(messageConverters, customizers);
        // init converters
        feignHttpMessageConverters.getConverters();
        return feignHttpMessageConverters;
    }
}
