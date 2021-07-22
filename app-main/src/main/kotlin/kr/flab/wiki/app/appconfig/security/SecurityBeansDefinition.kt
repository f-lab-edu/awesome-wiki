package kr.flab.wiki.app.appconfig.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.flab.wiki.app.components.authentication.AuthenticationProviderImpl
import kr.flab.wiki.app.components.authentication.UserAuthentication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * 스프링 시큐리티에 관한 빈 정의를 담은 클래스입니다.
 * 특정 시큐리티 관련 인터페이스나 추상클래스를 상속받지 않고 공통 사용하는 빈을 이곳에 작성합니다.
 */
@Configuration
class SecurityBeansDefinition {

    @Bean
    fun objectMapper() : ObjectMapper {
        return jacksonObjectMapper()
    }

    @Bean
    fun authenticationProvider(
        userAuthentication: UserAuthentication
    ): AuthenticationProvider {
        return AuthenticationProviderImpl(userAuthentication)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
