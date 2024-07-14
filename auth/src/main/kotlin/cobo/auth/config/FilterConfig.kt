package cobo.auth.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun logFilterRegistration(): FilterRegistrationBean<LogFilter> {
        val registrationBean = FilterRegistrationBean<LogFilter>()
        registrationBean.filter = LogFilter()
        registrationBean.addUrlPatterns("/*")
        registrationBean.order = 1

        return registrationBean
    }
}