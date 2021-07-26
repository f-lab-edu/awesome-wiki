package kr.flab.wiki.app.components.authentication

import kr.flab.wiki.app.api.user.request.LoginRequest
import kr.flab.wiki.app.api.user.response.LoginResponse
import kr.flab.wiki.app.utils.JwtUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class LoginUserService(
    private val authenticationManager: AuthenticationManager,
) {

    fun login(loginRequest: LoginRequest): LoginResponse? {
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )
        )
        if (authentication.principal == null) {
            return null
        }
        SecurityContextHolder.getContext().authentication = authentication
        return LoginResponse(email = authentication.name, token = JwtUtils.generateJws(authentication))
    }
}
