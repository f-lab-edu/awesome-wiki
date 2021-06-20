package kr.flab.wiki.core.testcase.document

import com.github.javafaker.Faker
import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.document.usecases.ShowDocumentHistoryUseCase
import kr.flab.wiki.core.testlib.document.Documents
import kr.flab.wiki.core.testlib.user.Users
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Tag(TAG_TEST_UNIT)
@DisplayName("문서 편집이력 조회 UseCase를 테스트한다.")
@Suppress("ClassName", "NonAsciiCharacters") // 테스트 표현을 위한 한글 사용
class ShowDocumentHistoryUseCaseTest {

    /**
     * 만약 해당 문서의 제목이 "한국"에서 "대한민국"으로 바뀌었다면, Document만으로 그 이력을 추적할 수 없으므로
     * (Document에서는 식별자인 title이 다르면 다른 객체이므로)
     * DocumentHistory 도메인이 필요하다.
     */

    private val faker = Faker.instance()

    @Mock
    private lateinit var docsRepo: DocumentRepository

    private lateinit var sut: ShowDocumentHistoryUseCase

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        this.sut = ShowDocumentHistoryUseCase.newInstance(docsRepo)
    }

    @Nested
    inner class `해당 문서의 이력 조회를 했을 때` {
        // given:
        val chosenDocumentTitle: String = faker.lorem().word()

        @Nested
        inner class `이력이 존재하면` {

            @Test
            fun `전체 편집 이력을 조회한다`() {
                // when:
                `when`(docsRepo.findAllHistoryByTitle(chosenDocumentTitle)).thenReturn(
                    mutableListOf(
                        Documents.randomDocumentHistory(
                            masterTitle = chosenDocumentTitle,
                            title = faker.lorem().word(),
                            creator = Users.randomUser(),
                            version = 1
                        ),
                        Documents.randomDocumentHistory(
                            masterTitle = chosenDocumentTitle,
                            title = faker.lorem().word(),
                            creator = Users.randomUser(),
                            version = 2
                        ),
                        Documents.randomDocumentHistory(
                            masterTitle = chosenDocumentTitle,
                            title = faker.lorem().word(),
                            creator = Users.randomUser(),
                            version = 3
                        )
                    )
                )

                // then:
                val documentHistory = sut.findDocumentHistory(chosenDocumentTitle)

                // expect:
                assertThat(documentHistory.all { document -> document.masterTitle == chosenDocumentTitle }, `is`(true))
                assertThat(documentHistory.distinctBy { it.version }.size, `is`(documentHistory.size))

            }

        }

        @Nested
        inner class `이력이 존재하지 않으면` {

            @Test
            fun `빈 리스트를 반환한다`() {
                // when:
                `when`(docsRepo.findAllHistoryByTitle(chosenDocumentTitle)).thenReturn(mutableListOf())

                // then:
                val documentHistory = sut.findDocumentHistory(chosenDocumentTitle)

                // expect:
                assertThat(documentHistory.isEmpty(), Matchers.`is`(true))

            }

        }

    }

}
