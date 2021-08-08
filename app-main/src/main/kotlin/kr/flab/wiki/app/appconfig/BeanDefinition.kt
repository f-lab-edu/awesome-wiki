package kr.flab.wiki.app.appconfig

import kr.flab.wiki.app.infrastructure.MySqlDocumentRepository
import kr.flab.wiki.app.infrastructure.MySqlUserRepository
import kr.flab.wiki.core.domain.document.DocumentSaveService
import kr.flab.wiki.core.domain.document.DocumentQueryService
import kr.flab.wiki.core.domain.user.UserQueryService
import kr.flab.wiki.core.domain.user.UserRegisterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanDefinition {
    @Bean
    fun userQueryService(
        @Autowired userRepository: MySqlUserRepository
    ): UserQueryService {
        return UserQueryService.newInstance(userRepository)
    }
    @Bean
    fun userRegisterService(
        @Autowired userRepository: MySqlUserRepository
    ): UserRegisterService {
        return UserRegisterService.newInstance(userRepository)
    }
    @Bean
    fun documentService(
        @Autowired documentRepository: MySqlDocumentRepository
    ): DocumentQueryService {
        return DocumentQueryService.newInstance(documentRepository)
    }

    @Bean
    fun postDocumentUseCase(
        @Autowired documentRepository: MySqlDocumentRepository
    ): DocumentSaveService {
        return DocumentSaveService.newInstance(documentRepository)
    }
}
