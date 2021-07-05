package kr.flab.wiki.app.appconfig.domain

import kr.flab.wiki.core.domain.user.repository.UserRepository
import kr.flab.wiki.core.domain.user.usecases.UserLoginUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DomainBeansDefinition {
    /**
     * 현재 UserRepository 의 경우 구현체 생성만 해놓고 Mocking 해서 테스트할 예정입니다.
     */
    @Bean
    fun userRepository(): UserRepository {
        return UserRepository.newInstance()
    }
    @Bean
    fun userLoginUseCase(userRepository: UserRepository): UserLoginUseCase {
        return UserLoginUseCase.newInstance(userRepository)
    }
}
