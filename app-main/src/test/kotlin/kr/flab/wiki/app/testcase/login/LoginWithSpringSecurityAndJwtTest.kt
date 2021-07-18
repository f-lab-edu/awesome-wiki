package kr.flab.wiki.app.testcase.login

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.javafaker.Faker
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import kr.flab.wiki.TAG_TEST_E2E
import kr.flab.wiki.app.components.authentication.UserAuthentication
import kr.flab.wiki.core.testlib.user.Users
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.mock.mockito.MockBean

@Tag(TAG_TEST_E2E)
@DisplayName("스프링 시큐리티와 JWT를 사용한 로그인 시나리오를 확인한다.")
@Suppress("ClassName", "NonAsciiCharacters") // 테스트 표현을 위한 한글 사용
@ExtendWith(SpringExtension::class)
@SpringBootTest(
    properties = ["baseUri=http://localhost", "port=8080"],
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
class LoginWithSpringSecurityAndJwtTest {

    /** 유저 로그인 시나리오
     *
     * 로그인 및 인증
     *   RS256 + JWT + Spring Security
     *
     * 로그인 이전
     * 1. email, password를 파라미터로 하는 로그인 요청이 오면 email과 password를 검증한다.
     * 2. email 혹은 password 중 하나라도 일치하지 않으면, Http 400을 반환한다.
     * 3. email과 password가 둘 다 유효하다면, Http 200, JWT 토큰을 반환한다.
     *
     * 로그인 이후
     * 1. 발행한 JWT 토큰을 매 요청마다 검증한다. (스프링 시큐리티에서 인증이 필요하다고 정한 요청에만)
     * 2. JWT 가 유효하면 정상적으로 요청을 수행하도록 한다.
     * 3. JWT 가 유효하지 않으면 Http 403을 반환한다.
     *
     */

    private val faker = Faker.instance()
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userAuthentication: UserAuthentication

    @Value("\${port}")
    private var port: Int = 0

    @Value("\${baseUri}")
    private lateinit var baseUri: String

    private lateinit var requestSpecification: RequestSpecification

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        this.objectMapper = jacksonObjectMapper()
        /**
         * 다음 코드는 rest assured 에서 log 범위를 설정합니다.
         */
        val logConfiguration = LogConfig.logConfig()
            .enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
        val restAssuredConfig = RestAssuredConfig.config().logConfig(logConfiguration)

        this.requestSpecification = RequestSpecBuilder()
            .setBaseUri(baseUri)
            .setPort(port)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .setConfig(restAssuredConfig)
            .build()
    }

    @Nested
    inner class 사용자는 {

        @Nested
        inner class `이메일과 비밀번호로 로그인 시도 했을 때` {

            private lateinit var email: String
            private lateinit var password: String
            private lateinit var body: MutableMap<String, String>

            @BeforeEach
            fun setup() {
                email = faker.internet().emailAddress()
                password = faker.internet().password()
                body = mutableMapOf(
                    "email" to email,
                    "password" to password
                )
            }

            @Nested
            inner class 유효하면 {

                @BeforeEach
                fun setup() {
                    `when`(userAuthentication.authenticateUser(email, password))
                        .thenReturn(Users.randomUser(emailAddress = email))
                }

                @Test
                fun `토큰을 반환한다`() {

                    Given {
                        spec(requestSpecification)
                        body(objectMapper.writeValueAsString(body))
                    } When {
                        post("/login")
                    } Then {
                        statusCode(200)
                        body("email", equalTo(email))
                        body("token", notNullValue())
                        log().all()
                    }

                }

            }

            /**
             * @SpringBootTest 에서도 MockBean 정상 동작 확인.
             * https://github.com/spring-projects/spring-boot/issues/12470
             * 위 issue에서 mockbean reset 시 @SpringBootTest 어노테이션을 Nested 클래스마다 선언하여
             * Spring context를 분리할 수 있음을 확인하였으나, 현재 테스트에선 딱히 그러지 않아도 mocking 이 잘 된다.
             */

            @Nested
            inner class `유효하지 않으면` {
                @BeforeEach
                fun setup() {
                    `when`(userAuthentication.authenticateUser(email, password))
                        .thenReturn(null)
                }

                @Test
                fun `400 을 반환한다`() {

                    Given {
                        spec(requestSpecification)
                        body(objectMapper.writeValueAsString(body))
                    } When {
                        post("/login")
                    } Then {
                        statusCode(400)
                        log().all()
                    }

                }

            }

        }
    }

    @Disabled
    @Nested
    inner class `인증이 필요한 API 는` {

        @Nested
        inner class `매 요청마다 토큰을 검증해서` {

            @Nested
            inner class 유효하면 {

                @Test
                fun `API 를 정상수행한다`() {

                }

            }

            @Nested
            inner class `유효하지 않으면` {

                @Test
                fun `403을 반환한다`() {

                }

            }

        }

    }
}
