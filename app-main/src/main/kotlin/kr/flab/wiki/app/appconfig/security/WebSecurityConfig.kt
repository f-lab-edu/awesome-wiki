package kr.flab.wiki.app.appconfig.security

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

/**
 * 스프링 시큐리티의 설정을 담은 클래스입니다.
 * 특히 WebSecurityConfigurerAdapter 는 하나의 설정 클래스에서만 구현가능하므로
 * 이 클래스에서만 설정하도록 합니다.
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val authenticationProvider: AuthenticationProvider
) : WebSecurityConfigurerAdapter() {
    /**
     * 스프링에서 기본 제공해주는 UserDetailsService 와 UserDetails 를 사용하지 않았습니다.
     * UserDetailsService 의 loadByUserName 은 username 만 받도록 설계되어 있는데,
     * 해당 프로젝트의 경우 도메인에서 login 시 email 과 password 를 둘 다 받는 것으로 정의했기 때문에
     * UserDetailsService 를 구현하지 않고 UserAuthentication class 에서 도메인 usecase 를 이용해 login 비즈니스 로직을 구현했습니다.
     * 또한, UserDetailsService 와 UserDetails 를 사용하지 않기 위해 AuthenticationProvider 를 custom 해서
     * authenticate 함수를 구현했습니다.
     */
    override fun configure(
        authenticationManager: AuthenticationManagerBuilder
    ) {
        authenticationManager
            .authenticationProvider(authenticationProvider)
    }

    /**
     * http 요청 설정
     */
    override fun configure(
        http: HttpSecurity
    ) {
        /**
         * 시큐리티는 csrf 공격에 대비해서 xsrf token을 requestHeader에 같이 전송해줘야
         * 요청을 허용해줍니다. 하지만 지금 단계에서는 개발 편의를 위해서 해당 기능을 disable 했습니다.
         */
        http.csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/login").permitAll()
            .anyRequest().authenticated()
    }
}
