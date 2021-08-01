package kr.flab.wiki.app.components.authentication

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import kr.flab.wiki.app.utils.JwtUtils
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwsAuthenticationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jws: String? = JwtUtils.parseJws(request.getHeader("Authorization") ?: "")
        var result: Jws<Claims>?
        if (jws != null) {
            result = JwtUtils.validateJws(jws)
            if (result != null) {
                val email = JwtUtils.getEmailFromJws(jws)
                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(email, null)
            }
        }
        filterChain.doFilter(request, response)
    }
}
