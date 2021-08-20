package kr.flab.wiki.core.testcase.document

import com.github.javafaker.Faker
import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.common.exception.document.DocumentConflictException
import kr.flab.wiki.core.common.exception.document.DocumentNotFoundException
import kr.flab.wiki.core.common.exception.document.InvalidBodyException
import kr.flab.wiki.core.common.exception.document.InvalidTitleException
import kr.flab.wiki.core.domain.document.Document
import kr.flab.wiki.core.domain.document.DocumentFormatPolicy
import kr.flab.wiki.core.domain.document.service.DocumentQueryService
import kr.flab.wiki.core.domain.document.service.DocumentSaveService
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
class SaveDocumentServiceTest {
    private val faker = Faker.instance()

    @Mock
    private lateinit var docsRepo: DocumentRepository

    private lateinit var documentSaveService: DocumentSaveService
    private lateinit var documentQueryService: DocumentQueryService
    private val version : Long = 1
    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        this.documentSaveService = DocumentSaveService.newInstance(docsRepo)
        this.documentQueryService = DocumentQueryService.newInstance(docsRepo)
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
            assertThrows(InvalidTitleException::class.java) { documentSaveService.saveDocument(title, body, creator, version) }
        }

        @Test
        fun `너무 길면 문서를 작성할 수 없다`() {
            // and:
            val title = faker.lorem().characters(
                DocumentFormatPolicy.DEFAULT_MAXIMUM_TITLE_LENGTH + 1
            )

            // expect:
            assertThrows(InvalidTitleException::class.java) { documentSaveService.saveDocument(title, body, creator, version) }
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
            assertThrows(InvalidBodyException::class.java) { documentSaveService.saveDocument(title, body, creator, version) }
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
            private var savedDocument: Document? = null

            @BeforeEach
            fun setupWhen(){
                // when:
                `when`(docsRepo.findByTitle(title)).thenReturn(previousDocument)

            }
            @Test
            fun `기존 내용을 수정할 수 있다`(){
                // then:
                savedDocument = documentSaveService.saveDocument(title, body, creator, version)

                assertThat(savedDocument?.version, `is`(not(1)))

            }
            @Test
            fun `다른 유저가 기존 내용을 수정할 수 있다`(){
                val otherCreator = Users.randomUser()

                //then:
                savedDocument = documentSaveService.saveDocument(title, body, otherCreator, version)
                assertThat(savedDocument?.lastContributor, `is`(otherCreator))

                assertThat(savedDocument?.version, `is`(not(1)))

            }
            @Test
            fun `기존 내용을 수정하고 버전이 1 증가한다`(){
                savedDocument = documentSaveService.saveDocument(title, body, creator,version)
                assertThat(savedDocument?.version, `is`(2))
            }
            @Test
            fun `버전이 다를 경우 DocumentConflictException 발생`(){
                val updatedDoc = DocumentEntity(
                    previousDocument.title,
                    previousDocument.body,
                    previousDocument.lastContributor,
                    previousDocument.updatedAt,
                    previousDocument.version+1)
                `when`(docsRepo.save(updatedDoc)).thenReturn(previousDocument)
                assertThrows(DocumentConflictException::class.java){
                    savedDocument = documentSaveService.saveDocument(title, body, creator, updatedDoc.version)
                    assertThat(savedDocument?.version, `is`(2))
                }

            }
            @AfterEach
            fun afterEach(){
                if(savedDocument != null) {
                    assertThat(savedDocument?.title, `is`(title))
                    assertThat(savedDocument?.title, `is`(previousDocument.title))
                }
            }
        }

        @Test
        fun `없다면 새로 작성할 수 있다`() {
            // when:
            `when`(docsRepo.findByTitle(title)).thenReturn(null)

            // then:
            val savedDocument = documentSaveService.saveDocument(title, body, creator, version)

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
                val documents = documentQueryService.findDocumentsByTitle(inputTitle)

                // expect:
                assertThat(documents.all { document -> document.title.contains(inputTitle) }, `is`(true))

            }

            @Test
            fun `존재하지 않는다면 빈 리스트를 반환한다`() {

                // when:
                `when`(docsRepo.findAllByTitle(inputTitle)).thenReturn(mutableListOf())

                // then:
                val documents = documentQueryService.findDocumentsByTitle(inputTitle)

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

            @Test
            fun `선택한 문서를 반환한다`() {

                // when:
                `when`(docsRepo.getByTitle(chosenTitle)).thenReturn(Documents.randomDocument(title = chosenTitle))

                // then:
                val document = documentQueryService.getDocumentByTitle(chosenTitle)

                // expect:
                assertThat(document.title, `is`(chosenTitle))

            }

        }

        @Nested
        inner class `선택한 문서가 존재하지 않으면` {

            @Test
            fun `예외를 발생한다`() {

                // when:
                `when`(docsRepo.getByTitle(chosenTitle)).thenThrow(DocumentNotFoundException())

                // expect:
                assertThrows(DocumentNotFoundException::class.java) { documentQueryService.getDocumentByTitle(chosenTitle) }

            }

        }


    }
}
