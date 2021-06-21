package kr.flab.wiki.core.testcase.document

import com.github.javafaker.Faker
import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.domain.document.usecases.ShowDocumentHistoryUseCase
import kr.flab.wiki.core.testlib.document.Documents
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.*
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@Tag(TAG_TEST_UNIT)
@DisplayName("문서 편집이력 조회 Use case 의 사양을 정의한다.")
@Suppress("ClassName", "NonAsciiCharacters") // 테스트 표현을 위한 한글 사용
class ShowDocumentHistoryUseCaseTest {

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
    inner class `위키 서비스의 모든 사용자는` {

        @Nested
        inner class `해당 문서의 이력 조회를 했을 때` {
            // given:
            val chosenDocumentTitle: String = faker.lorem().word()

            @Nested
            inner class `이력이 존재하면` {

                @Test
                fun `전체 편집 이력을 조회할 수 있다`() {
                    // when:
                    `when`(docsRepo.findAllHistoryByTitle(chosenDocumentTitle)).thenReturn(
                        mutableListOf(
                            Documents.randomDocumentHistory(
                                title = chosenDocumentTitle,
                            ),
                            Documents.randomDocumentHistory(
                                title = chosenDocumentTitle,
                            ),
                            Documents.randomDocumentHistory(
                                title = chosenDocumentTitle,
                            )
                        )
                    )

                    // then:
                    val documentHistory = sut.findDocumentHistory(chosenDocumentTitle)

                    // expect:
                    assertThat(documentHistory.all { document -> document.title == chosenDocumentTitle }, `is`(true))

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
                    assertThat(documentHistory.isEmpty(), `is`(true))

                }

            }

        }

    }

}
