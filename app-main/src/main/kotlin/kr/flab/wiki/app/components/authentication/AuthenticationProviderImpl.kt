package kr.flab.wiki.app.components.authentication

import kr.flab.wiki.core.domain.user.User
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class AuthenticationProviderImpl(
    private val userAuthentication: UserAuthentication
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        val email: String = authentication?.name.toString()
        val password: String = authentication?.credentials.toString()

        val user: User = userAuthentication.authenticateUser(email, password)
            ?: return UsernamePasswordAuthenticationToken(null, null)

        return UsernamePasswordAuthenticationToken(user.emailAddress, password)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return true
    }
}
