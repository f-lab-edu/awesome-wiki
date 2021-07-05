package kr.flab.wiki.app.api.user.request

data class LoginRequest(
    val email: String,
    val password: String,
)
