import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.http.Header
import io.restassured.specification.RequestSpecification
import kr.flab.wiki.app.WikiApplication
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.payload.RequestFieldsSnippet
import org.springframework.restdocs.payload.ResponseFieldsSnippet
import org.springframework.restdocs.snippet.Snippet
import spock.lang.Specification
import test.kr.flab.wiki.app.IntegrationTestConfiguration

import javax.annotation.Nonnull
import javax.annotation.Nullable

import static io.restassured.RestAssured.given
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration

/**
 * 통합 테스트 실행시마다 매번 SpringBootTest 를 설정하는 편은 번거롭기 때문에 테스트 케이스들은 가급적
 * 이 파일을 상속받는 형태로 작성해 주시기 바랍니다. 코드 공유를 위한 상속이므로 anti-pattern 이지만
 * Spring 의 test context 는 '상태' 를 포함하기 때문에 상태관리를 편리하게 하기 위한 장치입니다.
 *
 * 또한 이 클래스를 상속함으로써 모든 테스트 케이스들은 동일한 Spring boot context 아래에서 동작하게 되며
 * <code>@SpringBootTest</code> 로 선언한 test context 는 캐시되므로 테스트 실행 속도가 빨라지는 장점이 있습니다.
 *
 * 단점으로는 특정 테스트 케이스에서 static call 등으로 test context 를 오염시켰을 경우 이를 알아채기가
 * 어렵다는 점입니다. 따라서 테스트 작성시 static call 은 가급적이면 하면 안됩니다. 하더라도 반드시 cleanup 을 해 주세요.
 * (ex: SecurityContextHolder 이용 등)
 *
 * 이 클래스는 Spock framework 이용을 전제로 작성했습니다.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [
        WikiApplication.class,
        IntegrationTestConfiguration.class
])
class IntegrationTestBase extends Specification {
    public static String TEST_CLIENT_VERSION = "1.0.0"

    private static final Set<String> DECLARED_DOCUMENT_IDS = new HashSet()
    private static final String DEFAULT_HOST = "localhost"
    private static final int DEFAULT_PORT = 8080

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation()

    @LocalServerPort
    private int port

    /**
     * app-main 에서 선언한 ObjectMapper 를 그대로 이용하기 위한 선언.
     */
    @Autowired
    private ObjectMapper defaultObjMapper

    private RequestSpecification documentationSpec

    /**
     * spock 의 test lifecycle method. 상속받는 클래스들에서도 같은 이름의 메소드를 선언할 수 있습니다.
     * 그렇게 하는 경우, 이 메소드가 먼저 실행된 후 개별 테스트 케이스들의 setup 이 실행됩니다.
     */
    def setup() {
        // groovyc 에서 autowired field 를 찾지 못하는 문제가 있어 명시적 참조를 선언
        final mapper = defaultObjMapper

        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build()
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
                new ObjectMapperConfig().jackson2ObjectMapperFactory { cls, charset -> mapper }
        )
    }

    // region test helper methods (요청 및 응답 선언을 간편하게 하기 위함)

    final RequestSpecification jsonRequestSpec() {
        return jsonRequestSpec("")
    }

    final RequestSpecification jsonRequestSpec(final @Nonnull String documentId) {
        return jsonRequestSpec(documentId, null, null)
    }

    final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nonnull RequestFieldsSnippet reqDoc
    ) {
        return jsonRequestSpec(documentId, reqDoc, null)
    }

    final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nonnull ResponseFieldsSnippet respDoc
    ) {
        return jsonRequestSpec(documentId, null, respDoc)
    }

    final RequestSpecification jsonRequestSpec(
            final @Nonnull String documentId,
            final @Nullable RequestFieldsSnippet reqDoc,
            final @Nullable ResponseFieldsSnippet respDoc
    ) {
        if (DECLARED_DOCUMENT_IDS.contains(documentId)) {
            throw new IllegalArgumentException("Document id '$documentId' is already declared.")
        } else {
            if (!documentId.isEmpty()) {
                DECLARED_DOCUMENT_IDS.add(documentId)
            }
        }

        final List<Snippet> snippets = new ArrayList()
        if (reqDoc != null) {
            snippets.add(reqDoc)
        }
        if (respDoc != null) {
            snippets.add(respDoc)
        }
        final Snippet[] snippetsArray = snippets.toArray(new Snippet[snippets.size()]) as Snippet[]

        final String platformInfo = "'${System.getProperty("os.name")}' " +
                "${System.getProperty("os.version")} " +
                "${System.getProperty("os.arch")}"

        final baseReqSpec = given(this.documentationSpec).log().all()
                .port(port)
                .contentType(MediaType.APPLICATION_JSON.toString())
                .accept(MediaType.APPLICATION_JSON.toString())
                .header(new Header("User-Agent",
                        "Spring-RESTDocs; $TEST_CLIENT_VERSION; $platformInfo"))

        if (documentId.isEmpty()) {
            return baseReqSpec
        } else {
            def uriProcessor = modifyUris().host(DEFAULT_HOST)
            if (DEFAULT_PORT == 0) {
                uriProcessor.removePort()
            } else {
                uriProcessor.port(DEFAULT_PORT)
            }

            return baseReqSpec.filter(document(documentId,
                    preprocessRequest(prettyPrint(), uriProcessor),
                    preprocessResponse(prettyPrint()),
                    snippetsArray
            ))
        }
    }
    // endregion
}
