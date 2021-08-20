package kr.flab.wiki.app.appconfig

import kr.flab.wiki.core.domain.document.service.DocumentSaveService
import kr.flab.wiki.core.domain.document.service.DocumentQueryService
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.user.UserQueryService
import kr.flab.wiki.core.domain.user.UserRegisterService
import kr.flab.wiki.core.domain.user.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.inject.Inject

@Configuration
class BeanDefinition @Inject constructor(
    private val userRepository: UserRepository,
    private val documentRepository: DocumentRepository
) {
    @Bean
    fun userQueryService(): UserQueryService {
        return UserQueryService.newInstance(userRepository)
    }
    @Bean
    fun userRegisterService(): UserRegisterService {
        return UserRegisterService.newInstance(userRepository)
    }
    @Bean
    fun documentService(): DocumentQueryService {
        return DocumentQueryService.newInstance(documentRepository)
    }

    @Bean
    fun postDocumentUseCase(): DocumentSaveService {
        return DocumentSaveService.newInstance(documentRepository)
    }
}
