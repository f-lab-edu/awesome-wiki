package kr.flab.wiki.core.login.repository

interface SessionRepository {
    /**
     * @설명 : 세션에 값을 저장하고, username 을 반환한다.
     * @return : username : String
     */
    fun setAttribute(key : String, username : String) : String?
}
