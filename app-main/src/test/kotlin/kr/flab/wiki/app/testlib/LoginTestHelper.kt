package kr.flab.wiki.app.testlib

import io.restassured.RestAssured
import io.restassured.specification.RequestSpecification
import kr.flab.wiki.app.api.user.request.LoginRequest
import kr.flab.wiki.app.api.user.response.LoginResponse

object LoginTestHelper {

    fun makeLoginResponse(requestSpecification: RequestSpecification, email: String, password: String): LoginResponse {
        return RestAssured
            .given()
            .spec(requestSpecification)
            .body(LoginRequest(email, password))
            .`when`()
            .post("/login")
            .then()
            .extract()
            .`as`(LoginResponse::class.java)
    }

}
