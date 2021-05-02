package kr.flab.wiki.core.login.repository

interface SessionRepository {
    /**
     * @설명 : 세션에 값을 저장한다.
     * @return : username : String
     */
    fun setAttribute(key : String, username : String) : String?
}
