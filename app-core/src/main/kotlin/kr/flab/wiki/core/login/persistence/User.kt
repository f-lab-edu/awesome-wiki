package kr.flab.wiki.core.login.persistence

interface User {
    //이름
    val email : String
    //비밀번호
    val password : String
}
