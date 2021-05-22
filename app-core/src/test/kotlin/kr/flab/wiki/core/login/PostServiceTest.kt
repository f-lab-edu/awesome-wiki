package kr.flab.wiki.core.login

import kr.flab.wiki.TAG_TEST_UNIT
import kr.flab.wiki.core.post.business.PostService
import kr.flab.wiki.core.post.business.PostServiceImpl
import kr.flab.wiki.core.post.exception.PostValidationException
import kr.flab.wiki.core.post.persistence.Post
import kr.flab.wiki.core.post.persistence.User
import kr.flab.wiki.core.post.repository.PostRepository
import kr.flab.wiki.core.post.repository.PostRepositoryImpl
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThanOrEqualTo
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertThrows
import org.mockito.Mockito.*

@Tag(TAG_TEST_UNIT)
@Suppress("ClassName", "NonAsciiCharacters") // 테스트 표현을 위한 한글 사용
@DisplayName("PostService 의 동작 시나리오를 확인한다.")
class PostServiceTest {
    private lateinit var mockPostRepository: PostRepository
    private lateinit var sut: PostService

    @BeforeEach
    private fun setup() {
        mockPostRepository = mock(PostRepositoryImpl::class.java)
        sut = PostServiceImpl(mockPostRepository)
    }

    @Nested
    inner class `임의의 사용자가` {
        // given:
        private val user: User = createRandomPostUserEntity()

        @Nested
        inner class `제목이 100자 이상인 포스트를 등록하는 경우` {
            // given:
            private val post: Post = createRandomPostEntity(user, title = "a".repeat(120))

            @BeforeEach
            fun setupWhen() {
                `when`(mockPostRepository.save(post)).thenReturn(post)
            }

            @Test
            fun `Exception 이 발생한다`() {
                // then:
                val exception: PostValidationException =
                    assertThrows(PostValidationException::class.java) { sut.writePost(post) }

                // expect:
                assertThat(exception.message, `is`("제목은 100자 미만이어야 합니다."))
            }
        }

        @Nested
        inner class `본문이 10,000자 이상인 포스트를 등록하는 경우` {
            // given:
            private val post: Post = createRandomPostEntity(user, text = "abcdefghij".repeat(1000))

            @BeforeEach
            fun setupWhen() {
                `when`(mockPostRepository.save(post)).thenReturn(post)
            }

            @Test
            fun `Exception 이 발생한다`() {
                // then:
                val exception: PostValidationException =
                    assertThrows(PostValidationException::class.java) { sut.writePost(post) }

                // expect:
                assertThat(exception.message, `is`("내용은 10,000자 미만이어야 합니다."))
            }
        }

        @Nested
        inner class `중복된 제목으로 포스트를 등록하는 경우` {
            // given:
            private val postWithDuplicateTitleFirst = createRandomPostEntity(user, title = "duplicateTitle")
            private val postWithDuplicateTitleSecond = createRandomPostEntity(user, title = "duplicateTitle")

            @BeforeEach
            fun setupWhen() {
                `when`(mockPostRepository.save(postWithDuplicateTitleFirst)).thenReturn(postWithDuplicateTitleFirst)
            }

            @Test
            fun `Exception 이 발생한다`() {
                // given: "첫번째 생성"
                `when`(mockPostRepository.isTitleAlreadyExists(postWithDuplicateTitleFirst.title)).thenReturn(false)
                sut.writePost(postWithDuplicateTitleFirst)

                // when: "이제 첫번째 글 제목에 대해 '이미 존재함' 을 반환하도록 구성한다"
                `when`(mockPostRepository.isTitleAlreadyExists(postWithDuplicateTitleFirst.title)).thenReturn(true)

                // then:
                val exception: PostValidationException =
                    assertThrows(PostValidationException::class.java) { sut.writePost(postWithDuplicateTitleSecond) }

                // expect:
                assertThat(exception.message, `is`("이미 해당 제목으로 생성한 포스트가 존재합니다."))
            }
        }

        @Nested
        inner class `전체 Post 목록을 클릭하는 경우` {
            @BeforeEach
            fun setupWhen() {
                `when`(mockPostRepository.getPosts())
                    .thenReturn(
                        mutableListOf(
                            createRandomPostEntity(user),
                            createRandomPostEntity(user)
                        )
                    )
            }

            @Test
            fun `Post 리스트를 불러온다`() {
                // then:
                val posts: List<Post> = sut.getPosts()

                // expect:
                verify(mockPostRepository, times(1)).getPosts()
                assertThat(posts.size, `is`(2))
            }
        }

        @Nested
        @DisplayName("특정 Post의 제목을 클릭하는 경우")
        inner class `특정 Post의 제목을 클릭하는 경우` {
            // given:
            private val post: Post = createRandomPostEntity(user)

            @BeforeEach
            fun setupWhen() {
                `when`(mockPostRepository.getPost(post.id))
                    .thenReturn(post)
            }

            @Test
            fun `해당 Post의 상세정보 혹은 null을 반환한다`() {
                // then:
                val loadedPost: Post? = sut.getPost(post.id)

                // expect:
                verify(mockPostRepository, times(1)).getPost(post.id)
                assertThat(loadedPost?.text, `is`(post.text))
            }
        }

        @Nested
        inner class `특정 Post의 내용을 수정하는 경우` {
            // given:
            private val post: Post = createRandomPostEntity(user)
            private val randomEditedPost: Post = createRandomEditedPostEntity(user, post)

            @BeforeEach
            fun setupWhen() {
                `when`(mockPostRepository.getPost(post.id))
                    .thenReturn(post)
                `when`(mockPostRepository.editPost(randomEditedPost)).thenReturn(randomEditedPost)
            }

            @Test
            fun `해당 Post의 내용이 변경된다`() {
                // then:
                val editedPost: Post? = sut.editPost(randomEditedPost)

                // expect:
                verify(mockPostRepository, times(1)).editPost(randomEditedPost)
                assertThat(post.text, `is`(not(randomEditedPost.text)))
                assertThat(randomEditedPost.text, `is`(editedPost!!.text))
            }

            @Test
            fun `해당 Post의 version이 증가한다`() {
                // then:
                val editedPost: Post? = sut.editPost(randomEditedPost)

                // expect:
                verify(mockPostRepository, times(1)).editPost(randomEditedPost)
                assertThat(post.version, `is`(lessThanOrEqualTo(editedPost!!.version)))
                assertThat(randomEditedPost.version, `is`(lessThanOrEqualTo(editedPost!!.version)))
            }
        }
    }
}
