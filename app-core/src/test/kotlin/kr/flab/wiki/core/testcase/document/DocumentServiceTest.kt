package kr.flab.wiki.core.testcase.document

import com.github.javafaker.Faker
import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.common.exception.document.DocumentNotFoundException
import kr.flab.wiki.core.common.exception.document.InvalidBodyException
import kr.flab.wiki.core.common.exception.document.InvalidTitleException
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentFormatPolicy
import kr.flab.wiki.core.domain.document.DocumentService
import kr.flab.wiki.core.domain.document.persistence.DocumentEntity
import kr.flab.wiki.core.domain.document.repository.DocumentRepository
import kr.flab.wiki.core.testlib.document.Documents
import kr.flab.wiki.core.testlib.user.Users
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.not
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

@Tag(TAG_TEST_UNIT)
@DisplayName("DocumentService 의 동작 시나리오를 확인한다.")
@Suppress("ClassName", "NonAsciiCharacters") // 테스트 표현을 위한 한글 사용
class DocumentServiceTest {
    private val faker = Faker.instance()

    @Mock
    private lateinit var docsRepo: DocumentRepository

    private lateinit var sut: DocumentService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        this.sut = DocumentService.newInstance(docsRepo)
    }

    @Nested
    inner class `문서 제목이` {
        // given:
        private val body = faker.lorem().paragraph()
        private val creator = Users.randomUser()

        @Test
        fun `없으면 문서를 작성할 수 없다`() {
            // and:
            val title = ""

            // expect:
            assertThrows(InvalidTitleException::class.java) { sut.saveDocument(title, body, creator) }
        }

        @Test
        fun `너무 길면 문서를 작성할 수 없다`() {
            // and:
            val title = faker.lorem().characters(
                DocumentFormatPolicy.DEFAULT_MAXIMUM_TITLE_LENGTH + 1
            )

            // expect:
            assertThrows(InvalidTitleException::class.java) { sut.saveDocument(title, body, creator) }
        }
    }

    @Nested
    inner class `본문의 내용이` {
        private val title = faker.lorem().word()
        private val creator = Users.randomUser()

        @Test
        fun `너무 길면 문서를 작성할 수 없다`() {
            // and:
            val body = faker.lorem().characters(
                DocumentFormatPolicy.DEFAULT_MAXIMUM_BODY_LENGTH + 1
            )

            // expect:
            assertThrows(InvalidBodyException::class.java) { sut.saveDocument(title, body, creator) }
        }
    }

    @Nested
    inner class `같은 제목의 문서가` {
        // given:
        private val title = faker.lorem().word()
        private val body = faker.lorem().paragraph()
        private val creator = Users.randomUser()

        @BeforeEach
        fun setupWhen() {
            `when`(docsRepo.save(any())).thenAnswer { it.arguments[0] as DocumentEntity }
        }

        @Nested
        inner class 있다면 {
            private val previousDocument = Documents.randomDocument(title = title, version = 1)
            private lateinit var savedDocument: Document
            @BeforeEach
            fun setupWhen(){
                // when:
                `when`(docsRepo.findByTitle(title)).thenReturn(previousDocument)
            }
            @Test
            fun `기존 내용을 수정할 수 있다`(){
                // then:
                savedDocument = sut.saveDocument(title, body, creator)

                assertThat(savedDocument.version, `is`(not(1)))

            }
            @Test
            fun `다른 유저가 기존 내용을 수정할 수 있다`(){
                val otherCreator = Users.randomUser()

                //then:
                savedDocument = sut.saveDocument(title, body, otherCreator)
                assertThat(savedDocument.creator, `is`(otherCreator))

                assertThat(savedDocument.version, `is`(not(1)))

            }
            @Test
            fun `기존 내용을 수정하고 버전이 1 증가한다`(){
                savedDocument = sut.saveDocument(title, body, creator)
                assertThat(savedDocument.version, `is`(2))
            }
            @AfterEach
            fun afterEach(){
                assertThat(savedDocument.title, `is`(title))
                assertThat(savedDocument.title, `is`(previousDocument.title))
            }
        }

        @Test
        fun `없다면 새로 작성할 수 있다`() {
            // when:
            `when`(docsRepo.findByTitle(title)).thenReturn(null)

            // then:
            val savedDocument = sut.saveDocument(title, body, creator)

            // expect:
            assertThat(savedDocument.version, `is`(1))
        }
    }

    @Nested
    inner class `제목을 검색했을 때` {

        // given:
        private val inputTitle = faker.lorem().word()

        @Nested
        inner class `입력된 제목을 포함하는 문서가` {

            @Test
            fun `존재하면 문서 리스트를 반환한다`() {

                // when:
                `when`(docsRepo.findAllByTitle(inputTitle)).thenReturn(
                    mutableListOf(
                        Documents.randomDocument(title = inputTitle + faker.lorem().word()),
                        Documents.randomDocument(title = faker.lorem().word() + inputTitle),
                        Documents.randomDocument(title = faker.lorem().word() + inputTitle + faker.lorem().word())
                    )
                )

                // then:
                val documents = sut.findDocumentsByTitle(inputTitle)

                // expect:
                assertThat(documents.all { document -> document.title.contains(inputTitle) }, `is`(true))

            }

            @Test
            fun `존재하지 않는다면 빈 리스트를 반환한다`() {
                // when:
                `when`(docsRepo.findAllByTitle(inputTitle)).thenReturn(mutableListOf())

                // then:
                val documents = sut.findDocumentsByTitle(inputTitle)

                // expect:
                assertThat(documents.isEmpty(), `is`(true))

            }

        }

    }

    @Nested
    inner class `문서 리스트 중 하나를 선택하면` {

        // given:
        private val chosenTitle = faker.lorem().word()

        @Nested
        inner class `선택한 문서가 존재하면` {
            val lastVersion = 4L
            val lastRevision = Documents.randomDocument(title=chosenTitle, version = lastVersion)
            @BeforeEach
            fun beforeEach(){
                // when:
                `when`(docsRepo.getByTitle(chosenTitle)).thenReturn(lastRevision)
            }
            @Test
            fun `선택한 문서를 반환한다`() {
                // then:
                val document = sut.getDocumentByTitle(chosenTitle)

                // expect:
                assertThat(document.title, `is`(chosenTitle))

            }

            @Nested
            inner class `문서의 현재 변경 내역 조회 시`{
                val firstVersion = 1L
                @Test
                fun `처음부터 끝까지 조회시 범위에 해당하는 변경내역을 반환`(){
                    val versionList = listOf(firstVersion, firstVersion+1, firstVersion+2, firstVersion+3)
                    `when`(docsRepo.findHistoryByTitle(chosenTitle, firstVersion, lastVersion)).thenReturn(
                        listOf(
                            Documents.randomDocument(title = chosenTitle, version = versionList[0]),
                            Documents.randomDocument(title = chosenTitle, version = versionList[1]),
                            Documents.randomDocument(title = chosenTitle, version = versionList[2]),
                            lastRevision,
                        )
                    )
                    val history = sut.findDocumentHistory(chosenTitle, firstVersion, lastVersion)
                    assertThat(history.all { document -> versionList.contains(document.version) }, `is`(true))
                }
            }
        }

        @Nested
        inner class `선택한 문서가 존재하지 않으면` {

            @Test
            fun `예외를 발생한다`() {

                // when:
                `when`(docsRepo.getByTitle(chosenTitle)).thenThrow(DocumentNotFoundException())

                // expect:
                assertThrows(DocumentNotFoundException::class.java) { sut.getDocumentByTitle(chosenTitle) }

            }

        }
    }


}
