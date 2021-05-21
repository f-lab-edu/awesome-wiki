package kr.flab.wiki.core.login

import kr.flab.wiki.core.post.business.PostService
import kr.flab.wiki.core.post.business.PostServiceImpl
import kr.flab.wiki.core.post.exception.PostValidationException
import kr.flab.wiki.core.post.persistence.Post
import kr.flab.wiki.core.post.persistence.PostEntity
import kr.flab.wiki.core.post.persistence.User
import kr.flab.wiki.core.post.repository.PostRepository
import kr.flab.wiki.core.post.repository.PostRepositoryImpl
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.*
import java.time.LocalDateTime

//한글 사용을 위해 추가
@Suppress("NonAsciiCharacters")
@DisplayName("해당 테스트는 PostService 메서드를 테스트하는 클래스이다.")
class PostServiceTest {

    lateinit var mockPostRepository: PostRepository
    lateinit var sut: PostService

    @BeforeEach
    private fun setup() {
        mockPostRepository = mock(PostRepositoryImpl::class.java)
        sut = PostServiceImpl(mockPostRepository)
    }

    @Nested
    @DisplayName("임의의 사용자가")
    inner class `임의의 사용자가` {

        //given
        private val user: User = createRandomPostUserEntity()

        @Nested
        @DisplayName("제목이 100자 이상인 포스트를 등록하는 경우")
        inner class `제목이 100자 이상인 포스트를 등록하는 경우` {

            //given
            private val post: Post = createRandomPostEntity(user, title = "a".repeat(120))

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.save(post)).thenReturn(post)
            }

            @Test
            @DisplayName("Exception이 발생한다")
            fun `Exception이 발생한다`() {
                //when
                val exception: PostValidationException =
                    assertThrows(PostValidationException::class.java) { sut.writePost(post) }
                //then
                assertThat(exception.message, `is`("제목은 100자 미만이어야 합니다."))

            }
        }

        @Nested
        @DisplayName("본문이 10,000자 이상인 포스트를 등록하는 경우")
        inner class `본문이 10,000자 이상인 포스트를 등록하는 경우` {

            //given
            private val post: Post = createRandomPostEntity(user, text = "abcdefghij".repeat(1000))

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.save(post)).thenReturn(post)
            }

            @Test
            @DisplayName("Exception이 발생한다")
            fun `Exception이 발생한다`() {
                //when
                val exception: PostValidationException =
                    assertThrows(PostValidationException::class.java) { sut.writePost(post) }
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
                `when`(mockPostRepository.save(postWithDuplicateTitleFirst)).thenReturn(postWithDuplicateTitleFirst)
            }

            @Test
            @DisplayName("Exception이 발생한다")
            fun `Exception이 발생한다`() {
                //when
                //첫번째 생성
                val firstWrittenPost = sut.writePost(postWithDuplicateTitleFirst)
                //생성한 경우 isTitleAlreadyExists는 true를 반환하게 된다.
                if (firstWrittenPost != null) {
                    `when`(mockPostRepository.isTitleAlreadyExists(postWithDuplicateTitleFirst.title)).thenReturn(true)
                }

                val exception: PostValidationException =
                    assertThrows(PostValidationException::class.java) { sut.writePost(postWithDuplicateTitleSecond) }

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
                    .thenReturn(
                        mutableListOf(
                            createRandomPostEntity(user),
                            createRandomPostEntity(user)
                        )
                    )
            }

            @Test
            @DisplayName("Post 리스트를 불러온다.")
            fun `Post 리스트를 불러온다`() {
                //when
                val posts: List<Post> = sut.getPosts()

                //then
                verify(mockPostRepository, times(1)).getPosts()
                assertThat(posts.size, `is`(2))

            }

        }

        @Nested
        @DisplayName("특정 Post의 제목을 클릭하는 경우")
        inner class `특정 Post의 제목을 클릭하는 경우` {

            //given
            private val post: Post = createRandomPostEntity(user)

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.getPost(post.id))
                    .thenReturn(post)
            }

            @Test
            @DisplayName("해당 Post의 상세정보 혹은 null을 반환한다.")
            fun `해당 Post의 상세정보 혹은 null을 반환한다`() {
                //when
                val loadedPost: Post? = sut.getPost(post.id)

                //then
                verify(mockPostRepository, times(1)).getPost(post.id)
                assertThat(loadedPost?.text, `is`(post.text))
            }
        }

        @Nested
        @DisplayName("특정 Post의 내용을 수정하는 경우")
        inner class `특정 Post의 내용을 수정하는 경우` {

            //given
            private val post: Post = createRandomPostEntity(user)

            //given
            private val randomEditedPost: Post = createRandomEditedPostEntity(user, post)

            @BeforeEach
            fun mocking() {
                `when`(mockPostRepository.getPost(post.id))
                    .thenReturn(post)
                `when`(mockPostRepository.editPost(randomEditedPost)).thenReturn(randomEditedPost)
            }

            @Test
            @DisplayName("해당 Post의 내용이 변경된다.")
            fun `해당 Post의 내용이 변경된다`() {

                //when
                val editedPost: Post? = sut.editPost(randomEditedPost)

                //then
                verify(mockPostRepository, times(1)).editPost(randomEditedPost)
                assertThat(post.text, `is`(not(randomEditedPost.text)))
                assertThat(randomEditedPost.text, `is`(editedPost!!.text))

            }

            @Test
            @DisplayName("해당 Post의 version이 증가한다.")
            fun `해당 Post의 version이 증가한다`() {

                //when
                val editedPost: Post? = sut.editPost(randomEditedPost)

                //then
                verify(mockPostRepository, times(1)).editPost(randomEditedPost)
                assertThat(post.version, `is`(lessThanOrEqualTo(editedPost!!.version)))
                assertThat(randomEditedPost.version, `is`(lessThanOrEqualTo(editedPost!!.version)))

            }

        }

    }
}
