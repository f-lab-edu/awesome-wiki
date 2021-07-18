package kr.flab.wiki.app.components.authentication

import kr.flab.wiki.core.domain.user.User
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class AuthenticationProviderImpl(
    private val userAuthentication: UserAuthentication
) : AuthenticationProvider {
    override fun authenticate(authentication: Authentication?): Authentication {
        val user: User = userAuthentication.authenticateUser(
            authentication?.name.toString(),
            authentication?.credentials.toString()
        ) ?: return UsernamePasswordAuthenticationToken(null, null)

        return UsernamePasswordAuthenticationToken(user.emailAddress, null)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return true
    }
}
