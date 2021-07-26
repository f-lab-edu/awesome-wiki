package kr.flab.wiki.app.api.user.response

data class LoginResponse(
    val email: String,
    val token: String,
)
