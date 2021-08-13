package kr.flab.wiki.app.api.user.loginUser

import kr.flab.wiki.app.api.user.request.LoginRequest
import kr.flab.wiki.app.api.user.response.LoginResponse
import kr.flab.wiki.app.components.authentication.LoginUserService
import kr.flab.wiki.app.type.annotation.ApiHandler
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@ApiHandler
@RestController
class LoginUserApi(
    private val loginUserService: LoginUserService,
) {
    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        val loginResponse: LoginResponse = loginUserService.login(loginRequest)
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        return ResponseEntity.ok().body(loginResponse)
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test")
    fun test(): ResponseEntity<Any> {
        return ResponseEntity.ok().body("test")
    }
}
