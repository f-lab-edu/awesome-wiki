package kr.flab.wiki.app.appconfig

import kr.flab.wiki.app.infrastructure.MySqlUserRepository
import kr.flab.wiki.core.domain.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanDefinition {
    @Bean
    fun userService(
        @Autowired userRepository: MySqlUserRepository
    ): UserService {
        return UserService.newInstance(userRepository)
    }
}
