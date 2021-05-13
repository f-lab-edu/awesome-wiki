package kr.flab.wiki.core.login

import kr.flab.wiki.core.post.business.PostService
import kr.flab.wiki.core.post.business.PostServiceImpl
import kr.flab.wiki.core.post.exception.PostValidationException
import kr.flab.wiki.core.post.persistence.Post
import kr.flab.wiki.core.post.persistence.User
import kr.flab.wiki.core.post.repository.PostRepository
import kr.flab.wiki.core.post.repository.PostRepositoryImpl
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.*

//한글 사용을 위해 추가
@Suppress("NonAsciiCharacters")
@DisplayName("해당 테스트는 PostService 메서드를 테스트하는 클래스이다.")
class PostServiceTest {

    lateinit var mockPostRepository: PostRepository
    lateinit var sut : PostService

    @BeforeEach
    private fun setup(){
        mockPostRepository = mock(PostRepositoryImpl::class.java)
        sut = PostServiceImpl(mockPostRepository)
    }

    @Nested
    @DisplayName("임의의 사용자가")
    inner class `임의의 사용자가` {

        //given
        private val user : User = createRandomPostUserEntity()

        @Nested
        @DisplayName("제목이 100자 이상인 포스트를 등록하는 경우")
        inner class `제목이 100자 이상인 포스트를 등록하는 경우` {

            //given
            private val post : Post = createRandomPostEntity(user, title = "a".repeat(120))

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.save(post)).thenReturn(true)
            }

            @Test
            @DisplayName("Exception이 발생한다")
            fun `Exception이 발생한다`() {
                //when
                val exception : PostValidationException = assertThrows(PostValidationException::class.java) {sut.writePost(post)}
                //then
                assertThat(exception.message, `is`("제목은 100자 미만이어야 합니다."))

            }
        }

        @Nested
        @DisplayName("본문이 10,000자 이상인 포스트를 등록하는 경우")
        inner class `본문이 10,000자 이상인 포스트를 등록하는 경우` {

            //given
            private val post : Post = createRandomPostEntity(user, text = "abcdefghij".repeat(1000))

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.save(post)).thenReturn(true)
            }

            @Test
            @DisplayName("Exception이 발생한다")
            fun `Exception이 발생한다`() {
                //when
                val exception : PostValidationException = assertThrows(PostValidationException::class.java) {sut.writePost(post)}
                //then
                assertThat(exception.message, `is`("내용은 10,000자 미만이어야 합니다."))
            }
        }

        @Nested
        @DisplayName("중복된 제목으로 포스트를 등록하는 경우")
        inner class `중복된 제목으로 포스트를 등록하는 경우` {

            //given
            private val postWithDuplicateTitleFirst = createRandomPostEntity(user, title = "duplicateTitle")
            private val postWithDuplicateTitleSecond = createRandomPostEntity(user, title = "duplicateTitle")

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.save(postWithDuplicateTitleFirst)).thenReturn(true)
            }

            @Test
            @DisplayName("Exception이 발생한다")
            fun `Exception이 발생한다`() {
                //when
                //첫번째 생성
                val isFirstWritten = sut.writePost(postWithDuplicateTitleFirst)
                //생성한 경우 isTitleAlreadyExists는 true를 반환하게 된다.
                if(isFirstWritten){
                    `when`(mockPostRepository.isTitleAlreadyExists(postWithDuplicateTitleFirst.title)).thenReturn(true)
                }

                val exception : PostValidationException = assertThrows(PostValidationException::class.java) {sut.writePost(postWithDuplicateTitleSecond)}

                //then
                assertThat(exception.message, `is`("이미 해당 제목으로 생성한 포스트가 존재합니다."))
            }

        }

        @Nested
        @DisplayName("전체 Post 목록을 클릭하는 경우")
        inner class `전체 Post 목록을 클릭하는 경우` {

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.getPosts())
                    .thenReturn(mutableListOf(
                        createRandomPostEntity(user),
                        createRandomPostEntity(user)
                    ))
            }

            @Test
            @DisplayName("Post 리스트를 불러온다.")
            fun `Post 리스트를 불러온다` () {
                //when
                val posts : List<Post> = sut.getPosts()

                //then
                verify(mockPostRepository, times(1)).getPosts()
                assertThat(posts.size, `is`(2))

            }

        }


    }
}
